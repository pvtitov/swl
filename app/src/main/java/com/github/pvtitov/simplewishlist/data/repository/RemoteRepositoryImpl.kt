package com.github.pvtitov.simplewishlist.data.repository

import com.github.pvtitov.simplewishlist.data.datasource.NoServerDataSource
import com.github.pvtitov.simplewishlist.data.mapper.toData
import com.github.pvtitov.simplewishlist.data.mapper.toDomain
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.repository.RemoteRepository
import com.github.pvtitov.simplewishlist.utils.Logger

class RemoteRepositoryImpl(
    private val dataSource: NoServerDataSource,
) : RemoteRepository {

    override suspend fun login(): Status<User> = runCatchingCancellable {
        dataSource.login().toDomain()
            .also { Logger.debug(TAG, "login() returns Status.Success($it)") }
    }

    override suspend fun logout(): Status<Unit> = runCatchingCancellable {
        dataSource.logout()
            .also { Logger.debug(TAG, "logout() returns Status.Success($it)") }
    }

    override suspend fun downloadMine(): Status<List<Wish>> = runCatchingCancellable {
        (dataSource.downloadMineWishList()?.wishList?.map { it.toDomain() }
            ?: emptyList())
            .also { Logger.debug(TAG, "downloadMine() returns Status.Success($it)") }
    }

    override suspend fun download(user: User): Status<List<Wish>> = runCatchingCancellable {
        val shareToken = user.toData().shareToken
        (dataSource.downloadFriendsWishList(shareToken)?.wishList?.map { it.toDomain() }
            ?: emptyList())
            .also { Logger.debug(TAG, "download() for user = $user returns Status.Success($it)") }
    }

    override suspend fun upload(wishList: List<Wish>): Status<List<Wish>> = runCatchingCancellable {
        val data = wishList.map { it.toData() }
        dataSource.uploadMineWishList(WishListDto(data))
        wishList
            .also { Logger.debug(TAG, "upload() for wishList = $wishList returns Status.Success($it)") }
    }

    companion object {
        private const val TAG = "RemoteRepositoryImpl"
    }
}