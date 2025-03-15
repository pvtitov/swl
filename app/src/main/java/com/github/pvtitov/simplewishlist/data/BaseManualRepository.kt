package com.github.pvtitov.simplewishlist.data

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.pvtitov.simplewishlist.domain.data.Repository
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import kotlin.coroutines.resume

abstract class BaseManualRepository<T>(activity: ComponentActivity) : Repository<T> {
    private val coroutineScope = activity.lifecycleScope
    private var downloadContinuation: CancellableContinuation<T?>? = null
    private val downloadLauncher = activity.registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val data = readFromFile(uri, activity.contentResolver)
                downloadContinuation?.resume(data)
            }
        }
    }

    private var dataToUpload: T? = null
    private var uploadContinuation: CancellableContinuation<Boolean>? = null
    private val uploadLauncher = activity.registerForActivityResult(
        ActivityResultContracts.CreateDocument(JSON_MIME_TYPE)
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val isUploaded = dataToUpload?.let {
                    writeToFile(it, uri, activity.contentResolver)
                } ?: return@launch
                uploadContinuation?.resume(isUploaded)
            }
        }
    }

    override suspend fun download(): T? =
        suspendCancellableCoroutine { continuation ->
            downloadContinuation = continuation
            downloadLauncher.launch(arrayOf(JSON_MIME_TYPE))
            downloadContinuation?.invokeOnCancellation {
                downloadContinuation?.resume(null)
                downloadLauncher.unregister()
            }
        }

    override suspend fun upload(data: T): Boolean {
        dataToUpload = data
        return suspendCancellableCoroutine { continuation ->
            uploadContinuation = continuation
            uploadLauncher.launch("")
            uploadContinuation?.invokeOnCancellation {
                uploadContinuation?.resume(false)
                uploadLauncher.unregister()
            }
        }
    }

    private suspend fun readFromFile(
        uri: Uri,
        contentResolver: ContentResolver
    ): T? {
        return withContext(Dispatchers.IO) {
            runCatching {
                val stringBuilder = StringBuilder()
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String? = reader.readLine()
                        while (line != null) {
                            stringBuilder.append(line)
                            line = reader.readLine()
                        }
                    }
                }
                deserialize(stringBuilder.toString())
            }.getOrNull()
        }
    }

    abstract fun deserialize(json: String): T?

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun writeToFile(
        data: T,
        uri: Uri,
        contentResolver: ContentResolver
    ): Boolean {
        return withContext(Dispatchers.IO.limitedParallelism(1)) {
            runCatching {
                contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
                    FileOutputStream(parcelFileDescriptor.fileDescriptor).use { outputStream ->
                        serialize(data)?.toByteArray(Charsets.UTF_8)?.let { bytes ->
                            outputStream.write(bytes)
                        }
                    }
                }
                true
            }.getOrDefault(false)
        }
    }

    abstract fun serialize(data: T): String?
}

private const val JSON_MIME_TYPE = "application/json"