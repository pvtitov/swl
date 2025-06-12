package com.github.pvtitov.noserver

import android.content.Context
import android.content.Intent
import com.github.pvtitov.noserver.NoServerActivity.Companion.EXTRA_INPUT_DATA

object NoServer {
    private const val JSON_MIME_TYPE = "text/json"

    // TODO data storage
    private lateinit var myUserName: String
    private val userData = mutableMapOf<String, UserData>()

    private val jsonParser = JsonParser()

    private var dataToUpload: JsonString? = null
    private var onUploadCallback: ((Boolean) -> Unit)? = null

    /**
     * First of all setup
     */
    fun setup(
        userName: String,
        fileName: String,
        folderUrl: String
    ) {
        myUserName = userName

        val oldData = userData[myUserName]?.dataJsonString

        userData[myUserName] = UserData(
            userName = userName,
            fileName = fileName,
            folderUrl = folderUrl,
            dataJsonString = oldData
        )
    }

    fun addFriend(
        userName: String,
        fileName: String,
        folderUrl: String
    ) {
        val oldData = userData[userName]?.dataJsonString

        userData[userName] = UserData(
            userName = userName,
            fileName = fileName,
            folderUrl = folderUrl,
            dataJsonString = oldData
        )
    }

    fun sendInvitation(context: Context) {
        val myUserData = userData[myUserName] ?: return
        val jsonString = jsonParser.toJson(myUserData) ?: return

        share(context, jsonString)
    }

    fun shareFriends(context: Context) {
        val friendsData = userData.filter { (userName, _) -> userName != myUserName }.values
        val jsonString = jsonParser.toJson(friendsData) ?: return

        share(context, jsonString)
    }

    inline fun <reified T> download(userName: String): T {
        TODO("Not implemented")
        /*
         *  1.
         */
    }

    fun save(data: JsonString) {
        dataToUpload = data
    }

    fun upload(
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        onUploadCallback = callback

        val data = dataToUpload
        if (data == null) {
            callback.invoke(false)
            return
        }

        val intent = Intent(context, NoServerActivity::class.java)
            .putExtra(EXTRA_INPUT_DATA, data.value)

        context.startActivity(intent)
    }

    internal fun onUpload(isSuccess: Boolean) {
        onUploadCallback?.invoke(isSuccess)
    }

    private fun share(context: Context, jsonString: String, title: String = "") {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, jsonString)
            type = JSON_MIME_TYPE
        }

        val shareIntent = Intent.createChooser(sendIntent, title)
        context.startActivity(shareIntent)
    }

    data class UserData(
        val userName: String,
        val fileName: String,
        val folderUrl: String,
        val dataJsonString: JsonString?
    )

    data class JsonString(val value: String)
}