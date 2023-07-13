package com.github.pvtitov.simplewishlist.domain.repository

import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface LocalRepository {
    suspend fun onLogin(user: User): Status<Unit>

    suspend fun onLogout(): Status<Unit>

    suspend fun getMyWishList(): Status<List<Wish>?>

    suspend fun getWishList(user: User): Status<List<Wish>?>

    suspend fun saveMyWishList(wishList: List<Wish>): Status<List<Wish>>

    suspend fun saveWishList(user: User, wishList: List<Wish>): Status<List<Wish>>

    suspend fun addFriend(user: User): Status<User>

    suspend fun getFriendList(): Status<List<User>?>
}