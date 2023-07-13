package com.github.pvtitov.simplewishlist.domain.interactor

import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface Interactor {
    suspend fun login(): Status<User>

    suspend fun logout(): Status<Unit>

    suspend fun getMyWishList(): Status<List<Wish>>

    suspend fun addWish(wish: Wish): Status<Unit>

    suspend fun editWish(originalWish: Wish, modifiedWish: Wish): Status<Unit>

    suspend fun deleteWish(wish: Wish): Status<Unit>

    suspend fun getMyFriendList(): Status<List<User>>

    suspend fun getWishList(user: User): Status<List<Wish>>

    suspend fun addFriend(user: User): Status<User>
}