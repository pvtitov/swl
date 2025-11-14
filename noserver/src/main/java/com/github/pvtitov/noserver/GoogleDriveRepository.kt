package com.github.pvtitov.noserver

import android.util.Log
import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GoogleDriveRepository<T>(
    val fileName: String,
    val initialData: T
) {
    /**
     * Check if file exists and create one otherwise. Call before any other action requiring an existing file like
     * [download], [upload], [addFriend].
     */
    suspend inline fun <reified T> initialize() {
        if (initialData !is T) return

        if (!isFileExists()) {
            createFile()
            upload<T>(initialData)
        }
    }

    suspend fun getMyLogin(): String? {
        Log.d(TAG, "getMyLogin() called")
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                    val login = try {
                        drive.about().get()
                            .setFields("user(emailAddress, me)")
                            .execute()
                            .user.emailAddress
                    } catch (e: IOException) {
                        Log.e(TAG, "Failed to get current user login", e)
                        null
                    }
                    continuation.resume(login)
                }
            }
        }
    }

    /**
     * Call without parameter to load currently authenticated user data or provider user's e-mail as [login]
     */
    suspend inline fun <reified T> download(login: String = AUTHENTICATED_USER): T? {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->

                    val searchQuery = if (login == AUTHENTICATED_USER) {
                        "name='$fileName' and 'me' in owners"
                    } else {
                        "name='$fileName' and '$login' in owners"
                    }
                    val file = try {
                        drive.files().list()
                            .setQ(searchQuery)
                            .setSpaces("drive")
                            .setFields("files(id, name, owners, ownedByMe, permissions)")
                            .execute()
                            .files
                            .firstOrNull()
                    } catch (e: IOException) {
                        Log.e(TAG, "Failed to download data for login $login", e)
                        null
                    }

                    Log.d(TAG, "download(): file = $file")

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
    }

    suspend inline fun <reified T> upload(
        data: T
    ): Boolean {
        Log.d(TAG, "upload() called: data = $data")
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                    val file = getMyFile(drive)

                    if (file != null) {
                        val jsonString = JsonUtils.toJson<T>(data)
                        val mediaContent = ByteArrayContent.fromString("application/json", jsonString)
                        val result = try {
                            drive.files().update(file.id, null, mediaContent).execute()
                            true
                        } catch (e: IOException) {
                            Log.e(TAG, "Failed to get current user login", e)
                            false
                        }
                        continuation.resume(result)
                    } else {
                        Log.e(TAG, "File '$fileName' not found for upload.")
                        continuation.resume(false)
                    }
                }
            }
        }
    }

    suspend fun addFriend(login: String): Boolean {
        Log.d(TAG, "addFriend() called for $login")
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                    try {
                        val permission = Permission().apply {
                            type = "user"
                            role = "reader"
                            emailAddress = login
                        }

                        val file = getMyFile(drive) ?: run {
                            continuation.resume(false)
                            return@runWithAuthorisation
                        }

                        drive.permissions().create(file.id, permission).execute()

                        Log.d(TAG, "Successfully added friend $login as a reader to your $fileName")
                        continuation.resume(true)
                    } catch (e: IOException) {
                        Log.e(TAG, "Failed to add friend $login", e)
                        continuation.resume(false)
                    }
                }
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            GoogleDriveAuthorizationManager.logout()
        }
    }

    @Deprecated(
        "Don't use directly from outside this class. " +
                "It is only public to let reified parameter for this or dependent functions"
    )
    suspend fun isFileExists(): Boolean {
        Log.d(TAG, "isFileExists() called")
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                    val file = getMyFile(drive)
                    Log.d(TAG, "isFileExists() = ${file != null}")
                    continuation.resume(file != null)
                }
            }
        }
    }

    @Deprecated(
        "Don't use directly from outside this class. " +
                "It is only public to let reified parameter for this or dependent functions"
    )
    fun getMyFile(drive: Drive) = try {
        drive.files().list()
            .setQ("name='$fileName' and 'me' in owners")
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()
            .files
            .firstOrNull()
    } catch (e: IOException) {
        Log.e(TAG, "Failed to get my file", e)
        null
    }

    @Deprecated(
        "Don't use directly from outside this class. " +
                "It is only public to let reified parameter for this or dependent functions"
    )
    suspend fun createFile(): Boolean {
        Log.d(TAG, "createFile() called")
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                GoogleDriveAuthorizationManager.runWithAuthorisation { drive: Drive ->
                    val result = try {
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
                        true
                    } catch (e: IOException) {
                        Log.e(TAG, "Failed to create a file", e)
                        false
                    }
                    continuation.resume(result)
                }
            }
        }
    }

    companion object {
        const val TAG = "GoogleDriveRepository"
        const val AUTHENTICATED_USER = ""
    }
}