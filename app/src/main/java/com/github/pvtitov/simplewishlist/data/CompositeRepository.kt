package com.github.pvtitov.simplewishlist.data

import android.content.Context
import android.util.Log
import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.domain.model.WishList


class CompositeRepository {

    private val googleDriveRepository by lazy {
        GoogleDriveRepository<WishList>(
            "user_test",
            "file_test",
            ""
        )
    }

    fun download(userName: String): WishList? {
        return googleDriveRepository.download(userName)
    }

    fun upload(context: Context, data: WishList) {
        googleDriveRepository.save(data)
        googleDriveRepository.upload(context) { isUploaded ->
            Log.d(TAG, "Upload ${if (isUploaded) "succeeded" else "failed"}")
        }
    }

    private companion object {
        private const val TAG = "CompositeRepository"
    }
}
