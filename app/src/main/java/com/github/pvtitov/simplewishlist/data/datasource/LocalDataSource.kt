package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.simplewishlist.data.model.FriendListDto
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto

interface LocalDataSource {
    suspend fun login(user: UserDto)

    suspend fun getMyWishList(): WishListDto?

    suspend fun getFriendsWishList(friendsShareToken: String): WishListDto?

    suspend fun saveMyWishList(wishList: WishListDto)

    suspend fun saveFriendsWishList(friendsShareToken: String, wishList: WishListDto)

    suspend fun addFriend(user: UserDto)

    suspend fun getFriendList(): FriendListDto?

    suspend fun logout()
}