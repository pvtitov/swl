package com.github.pvtitov.simplewishlist.domain.interactor

import com.github.pvtitov.simplewishlist.data.repository.LocalRepository
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepository
import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.utils.logger.Logger

class InteractorImpl(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val logger: Logger
) : Interactor {
    override suspend fun login(): Status<User> {
        logger.debug(TAG, "login()")
        return remoteRepository.login().also { result ->
            if (result is Status.Success) {
                localRepository.onLogin(result.result)
            }
        }
    }

    override suspend fun logout(): Status<Unit> {
        logger.debug(TAG, "logout()")
        val remoteResult = remoteRepository.logout()
        if (remoteResult is Status.Failure) {
            return Status.Failure(remoteResult.error)
        }
        val localResult = localRepository.onLogout()
        return if (localResult is Status.Failure) {
            Status.Failure(localResult.error)
        } else {
            Status.Success(Unit)
        }
    }

    override suspend fun getMyWishList(): Status<List<Wish>> {
        logger.debug(TAG, "getMyWishList()")
        val localStatus = localRepository.getMyWishList()
        val localWishList = (localStatus as? Status.Success)?.result
        return if (localWishList != null) {
            Status.Success(localWishList)
        } else {
            remoteRepository.downloadMine().also { status ->
                if (status is Status.Success) {
                    localRepository.saveMyWishList(status.result)
                }
            }
        }
    }

    override suspend fun addWish(wish: Wish): Status<Unit> {
        logger.debug(TAG, "addWish(), wish = $wish")
        val wishListStatus = getMyWishList()
        val wishList = when (wishListStatus) {
            is Status.Success -> wishListStatus.result
            is Status.Failure -> return Status.Failure(wishListStatus.error)
        }
        val modifiedWishList = wishList + wish
        val updateWishListStatus = localRepository.saveMyWishList(modifiedWishList)
        return when (updateWishListStatus) {
            is Status.Success -> Status.Success(Unit)
            is Status.Failure -> Status.Failure(updateWishListStatus.error)
        }
    }

    override suspend fun editWish(
        originalWish: Wish,
        modifiedWish: Wish
    ): Status<Unit> {
        logger.debug(TAG, "editWish(), original wish = $originalWish, modified wish = $modifiedWish")
        val wishListStatus = getMyWishList()
        val wishList = when (wishListStatus) {
            is Status.Success -> wishListStatus.result
            is Status.Failure -> return Status.Failure(wishListStatus.error)
        }
        val index = wishList.indexOf(originalWish)
        if (index == -1) return Status.Failure(IllegalStateException("element not found"))
        val modifiedWishList = wishList
            .toMutableList()
            .apply {
                removeAt(index)
                add(index, modifiedWish)
            }
            .toList()
        val updateWishListStatus = localRepository.saveMyWishList(modifiedWishList)
        return when (updateWishListStatus) {
            is Status.Success -> Status.Success(Unit)
            is Status.Failure -> Status.Failure(updateWishListStatus.error)
        }
    }

    override suspend fun deleteWish(wish: Wish): Status<Unit> {
        logger.debug(TAG, "deleteWish(), wish = $wish")
        val wishListStatus = getMyWishList()
        val wishList = when (wishListStatus) {
            is Status.Success -> wishListStatus.result
            is Status.Failure -> return Status.Failure(wishListStatus.error)
        }
        val index = wishList.indexOf(wish)
        if (index == -1) return Status.Failure(IllegalStateException("element not found"))
        val modifiedWishList = wishList
            .toMutableList()
            .apply {
                removeAt(index)
            }
            .toList()
        val updateWishListStatus = localRepository.saveMyWishList(modifiedWishList)
        return when (updateWishListStatus) {
            is Status.Success -> Status.Success(Unit)
            is Status.Failure -> Status.Failure(updateWishListStatus.error)
        }
    }

    override suspend fun getMyFriendList(): Status<List<User>> {
        logger.debug(TAG, "getMyFriendList()")
        return when (val status = localRepository.getFriendList()) {
            is Status.Failure -> Status.Failure(status.error)
            is Status.Success -> Status.Success(status.result ?: emptyList())
        }
    }

    override suspend fun getWishList(user: User): Status<List<Wish>> {
        logger.debug(TAG, "getWishList(), user = $user")
        val localStatus = localRepository.getWishList(user)
        val localWishList = (localStatus as? Status.Success)?.result
        return if (localWishList != null) {
            Status.Success(localWishList)
        } else {
            remoteRepository.download(user).also { status ->
                if (status is Status.Success) {
                    localRepository.saveWishList(user, status.result)
                }
            }
        }
    }

    override suspend fun addFriend(user: User): Status<User> {
        logger.debug(TAG, "addFriend(), user = $user")
        return localRepository.addFriend(user)
    }

    companion object {
        private const val TAG = "InteractorImpl"
    }
}