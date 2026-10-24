package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto

class NoServerDataSource {

    private val googleDriveRepository by lazy {
        GoogleDriveRepository(
            FILE_NAME,
            WishListDto(
                wishList = emptyList(),
            )
        )
    }

    private val initializer: (suspend () -> String) by lazy {
        suspend {
            googleDriveRepository.login<WishListDto>()
        }
    }

    suspend fun login(): UserDto {
        val shareToken = initializer()
        return UserDto(shareToken = shareToken)
    }

    suspend fun downloadMineWishList(): WishListDto? {
        return googleDriveRepository.download()
    }

    suspend fun downloadFriendsWishList(friendsShareToken: String): WishListDto? {
        return googleDriveRepository.download(friendsShareToken)
    }

    suspend fun uploadMineWishList(wishList: WishListDto) {
        val isUploaded = googleDriveRepository.upload<WishListDto>(wishList)
    }

    suspend fun logout() {
        googleDriveRepository.logout()
    }

    private companion object {
        private const val TAG = "NoServerDataSource"
        private const val FILE_NAME = "wishlist_1.awl"
    }
}