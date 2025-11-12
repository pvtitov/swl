package com.github.pvtitov.noserver

import android.content.Context
import android.util.Log
import com.google.api.services.drive.Drive
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GoogleDriveRepository<T>(
    val fileName: String
) {
    /**
     * Call without parameter to load currently authenticated user data or provider user's e-mail as [login]
     */
    suspend inline fun <reified T> download(login: String = AUTHENTICATED_USER): T? {
        return suspendCoroutine { continuation ->
            GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->

                // todo remove
                // for testing
                drive.files().list().setSpaces("drive").setFields("files(name, owners)").execute().files.forEach {
                    Log.d(TAG, "${it.name} - ${it.owners}")
                }

                val searchQuery = if (login == AUTHENTICATED_USER) {
                    "'$fileName' in name and 'me' in owners"
                } else {
                    "'$fileName' in name and '$login' in owners"
                }
                val file = drive.files().list()
                    .setQ(searchQuery)
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute()
                    .files
                    .firstOrNull()

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
    suspend fun prepare() {
        // TODO check file exists
        // TODO create one if needed
    }

    suspend fun addFriend(login: String) {
        // give your friend a permission to read your file
    }

    companion object {
        const val TAG = "GoogleDriveRepository"
        const val AUTHENTICATED_USER = ""
    }
}