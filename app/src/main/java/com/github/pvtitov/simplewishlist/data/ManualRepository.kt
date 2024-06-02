package com.github.pvtitov.simplewishlist.data

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.utils.DI
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import kotlin.coroutines.resume

class ManualRepository(activity: ComponentActivity) : Repository {
    private val jsonParser = DI.jsonParser
    private val coroutineScope = activity.lifecycleScope
    private var downloadContinuation: CancellableContinuation<Dto?>? = null
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

    private lateinit var dataToUpload: Dto
    private var uploadContinuation: CancellableContinuation<Boolean>? = null
    private val uploadLauncher = activity.registerForActivityResult(
        ActivityResultContracts.CreateDocument(JSON_MIME_TYPE)
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val isUploaded = writeToFile(dataToUpload, uri, activity.contentResolver)
                uploadContinuation?.resume(isUploaded)
            }
        }
    }

    override suspend fun import(): Dto? =
        suspendCancellableCoroutine { continuation ->
            downloadContinuation = continuation
            downloadLauncher.launch(arrayOf(JSON_MIME_TYPE))
            downloadContinuation?.invokeOnCancellation {
                downloadContinuation?.resume(null)
                downloadLauncher.unregister()
            }
        }

    override suspend fun export(data: Dto): Boolean {
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
    ): Dto? {
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
                jsonParser.fromJson(stringBuilder.toString(), Dto::class.java)
            }.getOrNull()
        }
    }

    private suspend fun writeToFile(
        data: Dto,
        uri: Uri,
        contentResolver: ContentResolver
    ): Boolean {
        return withContext(Dispatchers.IO) {
            runCatching {
                contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
                    FileOutputStream(parcelFileDescriptor.fileDescriptor).use { outputStream ->
                        jsonParser.toJson(data)?.toByteArray(Charsets.UTF_8)?.let { bytes ->
                            outputStream.write(bytes)
                        }
                    }
                }
                true
            }.getOrDefault(false)
        }
    }
}

private const val JSON_MIME_TYPE = "application/json"