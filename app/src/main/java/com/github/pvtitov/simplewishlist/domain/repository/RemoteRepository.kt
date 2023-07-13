package com.github.pvtitov.simplewishlist.domain.repository

import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface RemoteRepository {
    suspend fun login(): Status<User>

    suspend fun logout(): Status<Unit>

    suspend fun downloadMine(): Status<List<Wish>>

    suspend fun download(user: User): Status<List<Wish>>

    suspend fun upload(wishList: List<Wish>): Status<List<Wish>>
}