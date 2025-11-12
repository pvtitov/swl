package com.github.pvtitov.simplewishlist.data

import android.content.Context
import android.util.Log
import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.domain.model.WishList


class CompositeRepository {

    private val googleDriveRepository by lazy {
        GoogleDriveRepository<WishList>(FILE_NAME)
    }

    suspend fun download(userName: String): WishList? {
        Log.d(TAG, "download($userName)")
        return googleDriveRepository.download()
    }

    suspend fun upload(context: Context, data: WishList) {
        googleDriveRepository.upload(data, context) { isUploaded ->
            Log.d(TAG, "Upload ${if (isUploaded) "succeeded" else "failed"}")
        }
    }

    private companion object {
        private const val TAG = "CompositeRepository"
        private const val FILE_NAME = "wishlist.awl"
    }
}