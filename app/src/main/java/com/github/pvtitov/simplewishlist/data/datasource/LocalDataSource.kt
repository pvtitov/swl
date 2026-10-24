package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.simplewishlist.data.model.FriendListDto
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

class LocalDataSource {
    private val currentUser = AtomicReference<UserDto?>(null)
    private val myWishList = AtomicReference<WishListDto?>(null)
    private val friendList = AtomicReference<FriendListDto?>(null)
    private val friendsWishLists: MutableMap<String, WishListDto> = ConcurrentHashMap()

    suspend fun login(user: UserDto) {
        currentUser.set(user)
    }

    suspend fun getMyWishList(): WishListDto? {
        return myWishList.get()
    }

    suspend fun getFriendsWishList(friendsShareToken: String): WishListDto? {
        return friendsWishLists[friendsShareToken]
    }

    suspend fun saveMyWishList(wishList: WishListDto) {
        myWishList.set(wishList)
    }

    suspend fun saveFriendsWishList(friendsShareToken: String, wishList: WishListDto) {
        friendsWishLists[friendsShareToken] = wishList
    }

    suspend fun addFriend(user: UserDto) {
        friendList.getAndUpdate { dto -> dto?.copy(friendList = dto.friendList + user) }
    }

    suspend fun getFriendList(): FriendListDto? {
        return friendList.get()
    }

    suspend fun logout() {
        currentUser.set(null)
        myWishList.set(null)
        friendList.set(null)
    }
}