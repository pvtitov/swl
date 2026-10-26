package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto

interface RemoteDataSource {
    suspend fun login(): UserDto

    suspend fun downloadMineWishList(): WishListDto?

    suspend fun downloadFriendsWishList(friendsShareToken: String): WishListDto?

    suspend fun uploadMineWishList(wishList: WishListDto)

    suspend fun logout()
}