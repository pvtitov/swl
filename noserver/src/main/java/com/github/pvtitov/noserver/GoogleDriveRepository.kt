package com.github.pvtitov.noserver

import android.content.Context
import android.content.Intent


class GoogleDriveRepository(
    userName: String,
    fileName: String,
    folderUrl: String
) {
    // TODO data storage
    private val userData = mutableMapOf<String, UserData>()

    private var dataToUpload: JsonString? = null

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
        NoServerApplication.applicationContext?.let { context ->
            context.startActivity(
                Intent(context, NoServerActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(NoServerActivity.AUTHENTICATION_EXTRA_KEY, true)
                }
            )
        }
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



    data class UserData(
        val userName: String,
        val fileName: String,
        val folderUrl: String,
        val dataJsonString: JsonString?
    )

    data class JsonString(val value: String)

    companion object {
        private const val TAG = "GoogleDriveRepository"
    }
}