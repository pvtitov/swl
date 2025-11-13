package com.github.pvtitov.simplewishlist.data

import android.util.Log
import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.domain.model.WishList


class CompositeRepository {

    private val googleDriveRepository by lazy {
        GoogleDriveRepository(FILE_NAME, WishList(emptyList(), emptyList(), emptyMap()))
    }

    private var initializer: (suspend () -> Unit)? = suspend {
        Log.d(TAG, "initializer invoked")
        googleDriveRepository.initialize<WishList>()
        initializer = null
    }

    suspend fun downloadMine(): WishList? {
        Log.d(TAG, "downloadMine()")
        initializer?.invoke()
        return googleDriveRepository.download()
    }

    suspend fun download(login: String): WishList? {
        Log.d(TAG, "download($login)")
        initializer?.invoke()
        return googleDriveRepository.download(login)
    }

    suspend fun upload(data: WishList) {
        val isUploaded = googleDriveRepository.upload<WishList>(data)
        initializer?.invoke()
        Log.d(TAG, "Upload ${if (isUploaded) "succeeded" else "failed"}")
    }

    suspend fun addFriend(login: String): Boolean {
        Log.d(TAG, "addFriend($login)")
        initializer?.invoke()
        return googleDriveRepository.addFriend(login)
    }

    private companion object {
        private const val TAG = "CompositeRepository"
        private const val FILE_NAME = "wishlist.awl"
    }
}