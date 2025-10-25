package com.github.pvtitov.simplewishlist.data

import android.content.Context
import android.util.Log
import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.domain.model.WishList
import com.github.pvtitov.simplewishlist.utils.DI


class CompositeRepository {

    private val jsonParser = DI.jsonParser

    private val googleDriveRepository by lazy {
        GoogleDriveRepository(
            "user_test",
            "file_test",
            ""
        )
    }

    fun download(userName: String): WishList? {
        val json = googleDriveRepository.download(userName)?.value ?: return null
        return jsonParser.fromJson(json)
    }

    fun upload(context: Context, data: WishList) {
        val json = GoogleDriveRepository.JsonString(jsonParser.toJson(data) ?: return)
        googleDriveRepository.save(json)
        googleDriveRepository.upload(context) { isUploaded ->
            Log.d(TAG, "Upload ${if (isUploaded) "succeeded" else "failed"}")
        }
    }

    private companion object {
        private const val TAG = "CompositeRepository"
    }
}
