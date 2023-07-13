package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.noserver.GoogleDriveRepository
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.utils.DI

class RemoteDataSourceDefault() : RemoteDataSource {
    private val logger = DI.logger

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
            logger.debug(TAG, "googleDriveRepository.login<WishListDto>()")
            googleDriveRepository.login<WishListDto>()
        }
    }

    override suspend fun login(): UserDto {
        logger.debug(TAG, "login()")
        val shareToken = initializer()
        return UserDto(shareToken = shareToken)
    }

    override suspend fun downloadMineWishList(): WishListDto? {
        return googleDriveRepository.download()
    }

    override suspend fun downloadFriendsWishList(friendsShareToken: String): WishListDto? {
        return googleDriveRepository.download(friendsShareToken)
    }

    override suspend fun uploadMineWishList(wishList: WishListDto) {
        val isUploaded = googleDriveRepository.upload<WishListDto>(wishList)
    }

    override suspend fun logout() {
        googleDriveRepository.logout()
    }

    private companion object {
        private const val TAG = "RemoteDataSourceDefault"
        private const val FILE_NAME = "wishlist_2.awl"
    }
}