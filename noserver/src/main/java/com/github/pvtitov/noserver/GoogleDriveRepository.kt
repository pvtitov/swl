package com.github.pvtitov.noserver

import android.content.Context
import android.util.Log
import com.google.api.services.drive.Drive


class GoogleDriveRepository(
    userName: String,
    fileName: String,
    folderUrl: String
) {
    // TODO data storage
    private val userData = mutableMapOf<String, UserData>()

    private var dataToUpload: JsonString? = null

    private var drive: Drive? = null

    init {
        val oldData = userData[userName]?.dataJsonString

        userData[userName] = UserData(
            userName = userName,
            fileName = fileName,
            folderUrl = folderUrl,
            dataJsonString = oldData
        )
    }

    fun download(userName: String): JsonString? {
        val fileNames =
            AuthenticationManager.drive?.files()?.list()?.setSpaces("drive")?.execute()?.files?.map { it.name }
        Log.d(TAG, "for $userName: $fileNames")
        return null
    }

    fun save(data: JsonString) {
        dataToUpload = data
    }

    fun upload(
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        // TODO
    }

    private fun checkAuthorization() {

    }

    data class UserData(
        val userName: String,
        val fileName: String,
        val folderUrl: String,
        val dataJsonString: JsonString?
    )

    data class JsonString(val value: String)

    companion object {
        private const val TAG = "AuthenticationManager"
    }
}