package com.github.pvtitov.simplewishlist.data.datasource

import com.github.pvtitov.simplewishlist.data.model.FriendListDto
import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

class LocalDataSourceImpl : LocalDataSource {
    private val currentUser = AtomicReference<UserDto?>(null)
    private val myWishList = AtomicReference<WishListDto?>(null)
    private val friendList = AtomicReference<FriendListDto?>(null)
    private val friendsWishLists: MutableMap<String, WishListDto> = ConcurrentHashMap()

    override suspend fun login(user: UserDto) {
        currentUser.set(user)
    }

    override suspend fun getMyWishList(): WishListDto? {
        return myWishList.get()
    }

    override suspend fun getFriendsWishList(friendsShareToken: String): WishListDto? {
        return friendsWishLists[friendsShareToken]
    }

    override suspend fun saveMyWishList(wishList: WishListDto) {
        myWishList.set(wishList)
    }

    override suspend fun saveFriendsWishList(friendsShareToken: String, wishList: WishListDto) {
        friendsWishLists[friendsShareToken] = wishList
    }

    override suspend fun addFriend(user: UserDto) {
        friendList.getAndUpdate { dto -> dto?.copy(friendList = dto.friendList + user) }
    }

    override suspend fun getFriendList(): FriendListDto? {
        return friendList.get()
    }

    override suspend fun logout() {
        currentUser.set(null)
        myWishList.set(null)
        friendList.set(null)
    }
}