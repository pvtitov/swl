package com.github.pvtitov.simplewishlist.data.repository

import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSource
import com.github.pvtitov.simplewishlist.data.mapper.toData
import com.github.pvtitov.simplewishlist.data.mapper.toDomain
import com.github.pvtitov.simplewishlist.data.model.WishListDto
import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.repository.LocalRepository
import com.github.pvtitov.simplewishlist.utils.logger.Logger

class LocalRepository(
    private val dataSource: LocalDataSource,
    private val logger: Logger
) : LocalRepository {

    override suspend fun onLogin(user: User): Status<Unit> = runCatchingCancellable {
        dataSource.login(user.toData())
            .also { logger.debug(TAG, "onLogin() returns Status.Success($it)") }
    }

    override suspend fun onLogout(): Status<Unit> = runCatchingCancellable {
        dataSource.logout()
            .also { logger.debug(TAG, "onLogout() returns Status.Success($it)") }
    }

    override suspend fun getMyWishList(): Status<List<Wish>?> = runCatchingCancellable {
        dataSource.getMyWishList()?.wishList?.map { it.toDomain() }
            .also { logger.debug(TAG, "getMyWishList() returns Status.Success($it)") }
    }

    override suspend fun getWishList(user: User): Status<List<Wish>?> = runCatchingCancellable {
        val userDto = user.toData()
        dataSource.getFriendsWishList(userDto.shareToken)?.wishList?.map { it.toDomain() }
            .also { logger.debug(TAG, "getWishList() for user = $user returns Status.Success($it)") }
    }

    override suspend fun saveMyWishList(
        wishList: List<Wish>
    ): Status<List<Wish>> = runCatchingCancellable {
        val wishListDto = WishListDto(wishList.map { it.toData() })
        dataSource.saveMyWishList(wishListDto)
        wishList
            .also { logger.debug(TAG, "saveMyWishList() for wishList = $wishList returns Status.Success($it)") }
    }

    override suspend fun saveWishList(
        user: User,
        wishList: List<Wish>
    ): Status<List<Wish>> = runCatchingCancellable {
        val shareToken = user.toData().shareToken
        val wishListDto = WishListDto(wishList.map { it.toData() })
        dataSource.saveFriendsWishList(shareToken, wishListDto)
        wishList
            .also {
                logger.debug(
                    TAG,
                    "saveWishList() for user = $user, wishList = $wishList returns Status.Success($it)"
                )
            }
    }

    override suspend fun addFriend(user: User): Status<User> = runCatchingCancellable {
        dataSource.addFriend(user.toData())
        user
            .also { logger.debug(TAG, "addFriend() for user = $user returns Status.Success($it)") }
    }

    override suspend fun getFriendList(): Status<List<User>?> = runCatchingCancellable {
        dataSource.getFriendList()?.friendList?.map { it.toDomain() }
            .also { logger.debug(TAG, "getFriendList() returns Status.Success($it)") }
    }

    companion object {
        private const val TAG = "LocalRepositoryImpl"
    }
}