package com.github.pvtitov.noserver

import android.content.Context
import android.content.Intent

object NoServer {
    private var onUploadCallback: ((String?) -> Unit)? = null

    fun download(invitation: String): String {
        TODO("Not implemented")
    }

    fun upload(
        context: Context,
        data: String,
        onUpload: (String?) -> Unit
    ) {
        onUploadCallback = onUpload

        val intent = Intent(context, NoServerActivity::class.java)
            .putExtra(NoServerActivity.EXTRA_INPUT_DATA, data)

        context.startActivity(intent)
    }

    internal fun onUpload(dirUrl: String?) {
        onUploadCallback?.invoke(dirUrl)
    }
}