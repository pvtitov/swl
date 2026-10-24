package com.github.pvtitov.simplewishlist.data.repository

import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSource
import com.github.pvtitov.simplewishlist.data.mapper.toData
import com.github.pvtitov.simplewishlist.data.mapper.toDomain
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val dataSource: LocalDataSource,
) : LocalRepository {

    override suspend fun onLogin(user: User): Status<Unit> = runCatchingCancellable {
        dataSource.login(user.toData())
    }

    override suspend fun onLogout(): Status<Unit> = runCatchingCancellable {
        dataSource.logout()
    }

    override suspend fun getMyWishList(): Status<List<Wish>?> = runCatchingCancellable {
        dataSource.getMyWishList()?.wishList?.map { it.toDomain() }
    }

    override suspend fun getWishList(user: User): Status<List<Wish>?> = runCatchingCancellable {
        val userDto = user.toData()
        dataSource.getFriendsWishList(userDto.shareToken)?.wishList?.map { it.toDomain() }
    }

    override suspend fun saveMyWishList(
        wishList: List<Wish>
    ): Status<List<Wish>> = runCatchingCancellable {
        val wishListDto = WishListDto(wishList.map { it.toData() })
        dataSource.saveMyWishList(wishListDto)
        wishList
    }

    override suspend fun saveWishList(
        user: User,
        wishList: List<Wish>
    ): Status<List<Wish>> = runCatchingCancellable {
        val shareToken = user.toData().shareToken
        val wishListDto = WishListDto(wishList.map { it.toData() })
        dataSource.saveFriendsWishList(shareToken, wishListDto)
        wishList
    }

    override suspend fun addFriend(user: User): Status<User> = runCatchingCancellable {
        dataSource.addFriend(user.toData())
        user
    }

    override suspend fun getFriendList(): Status<List<User>?> = runCatchingCancellable {
        dataSource.getFriendList()?.friendList?.map { it.toDomain() }
    }
}