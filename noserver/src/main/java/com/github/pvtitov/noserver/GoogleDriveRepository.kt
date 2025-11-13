package com.github.pvtitov.noserver

import android.content.Context
import android.util.Log
import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GoogleDriveRepository<T>(
    val fileName: String,
    val initialData: T
) {
    /**
     * Call without parameter to load currently authenticated user data or provider user's e-mail as [login]
     */
    suspend inline fun <reified T> download(login: String = AUTHENTICATED_USER): T? {
        prepare<T>()
        return suspendCoroutine { continuation ->
            GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->

                val searchQuery = if (login == AUTHENTICATED_USER) {
                    "name='$fileName' and 'me' in owners"
                } else {
                    "name='$fileName' and '$login' in owners"
                }
                val file = drive.files().list()
                    .setQ(searchQuery)
                    .setSpaces("drive")
                    .setFields("files(id, name, owners, ownedByMe)")
                    .execute()
                    .files
                    .firstOrNull()

                Log.d(TAG, "download(): file = $file, name = ${file?.name}, id = ${file?.id}, owners = ${file?.owners}, owned by me = ${file?.ownedByMe}")

                val data = if (file != null) {
                    val outputStream = ByteArrayOutputStream()

                    drive.files().get(file.id).executeMediaAndDownloadTo(outputStream)

                    val rawJsonString = outputStream.toString()
                    Log.d(TAG, "File content: $rawJsonString")

                    JsonUtils.fromJson<T>(rawJsonString)
                } else {
                    null
                }

                continuation.resume(data)
            }
        }
    }

    suspend fun upload(
        data: T,
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        // TODO: by this point file should exist, upload data to file
    }

    /**
     * Check if file exists and create one otherwise
     */
    suspend inline fun <reified T> prepare() {
        if (initialData !is T) return

        withContext(Dispatchers.IO) {
            if (!isFileExists()) {
                createFile()
                uploadContent<T>(initialData)
            }
        }
    }

    suspend fun addFriend(login: String) {
        // give your friend a permission to read your file
    }

    suspend fun isFileExists(): Boolean {
        Log.d(TAG, "isFileExists() called")
        return suspendCoroutine { continuation ->
            GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                val file = drive.files().list()
                    .setQ("name='$fileName'")
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute()
                    .files
                    .firstOrNull()
                Log.d(TAG, "isFileExists() = ${file != null}")
//                val file = drive.files().list()
//                    .setSpaces("drive")
//                    .execute()
//                    .files
//                    .find { fileName == it.name }
                continuation.resume(file != null)
            }
        }
    }

    suspend fun createFile() {
        Log.d(TAG, "createFile() called")
        return suspendCoroutine { continuation ->
            GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                val me = drive.about().get()
                    .setFields("user(emailAddress, me)")
                    .execute().user

                Log.d(TAG, "createFile(): me = $me")

                val file = File().apply {
                    name = fileName
                }
                drive.files().create(file)
                    .setFields("id")
                    .execute()
                continuation.resume(Unit)
            }
        }
    }

    suspend inline fun <reified T> uploadContent(data: T): Boolean {
        Log.d(TAG, "uploadContent() called: data = $data")
        return suspendCoroutine { continuation ->
            GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                val file = drive.files().list()
                    .setQ("name='$fileName' and 'me' in owners")
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute()
                    .files
                    .firstOrNull()

                if (file != null) {
                    val jsonString = JsonUtils.toJson<T>(data)
                    val mediaContent = ByteArrayContent.fromString("application/json", jsonString)
                    drive.files().update(file.id, null, mediaContent).execute()
                    continuation.resume(true)
                } else {
                    Log.e(TAG, "File '$fileName' not found for upload.")
                    continuation.resume(false)
                }
            }
        }
    }

    companion object {
        const val TAG = "GoogleDriveRepository"
        const val AUTHENTICATED_USER = ""
    }
}