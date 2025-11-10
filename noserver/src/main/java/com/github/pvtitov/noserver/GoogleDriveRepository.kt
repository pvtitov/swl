package com.github.pvtitov.noserver

import android.content.Context
import android.util.Log
import com.google.api.services.drive.Drive


class GoogleDriveRepository<T>(
    userName: String,
    fileName: String,
    folderUrl: String
) {
    // TODO data storage
    private val userData = mutableMapOf<String, UserData<T>>()

    private var dataToUpload: T? = null

    init {
        val oldData = userData[userName]?.data

        userData[userName] = UserData(
            userName = userName,
            fileName = fileName,
            folderUrl = folderUrl,
            data = oldData
        )
    }

    fun download(userName: String): T? {
        NoServerActivity.runWithAuthorisation { drive: Drive ->
            Log.d(TAG, drive.files().list().setSpaces("drive").execute().files.map { it.name }.toString())
        }
        return null
    }

    fun save(data: T) {
        dataToUpload = data
    }

    fun upload(
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        // TODO
    }



    data class UserData<T>(
        val userName: String,
        val fileName: String,
        val folderUrl: String,
        val data: T?
    )

    companion object {
        private const val TAG = "GoogleDriveRepository"
    }
}