package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.model.WishList
import com.github.pvtitov.simplewishlist.ui.model.*
import com.github.pvtitov.simplewishlist.utils.DI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _compositeRepository by lazy { DI.compositeRepository }

    private val _currentUserState: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentLoginFlow: StateFlow<String?> = _currentUserState
        .map { it?.name }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _errorState = MutableStateFlow<Error?>(null)
    val errorState: StateFlow<Error?> = _errorState.asStateFlow()

    private var downloadedWishList: WishList? = null
        set(value) {
            field = value
            modifiedWishList = value
        }
    private var modifiedWishList: WishList? = null
        set(value) {
            field = value
            _isWishListUpdatedState.value = value != null && value != downloadedWishList
        }

    private val _isWishListUpdatedState = MutableStateFlow(false)
    val isWishListUpdatedState: StateFlow<Boolean> = _isWishListUpdatedState.asStateFlow()

    private suspend fun upload() {
        val data = modifiedWishList ?: return

        val isUploaded = withContext(Dispatchers.Main) {
            _compositeRepository.upload(data)
            true
        }
        if (isUploaded) {
            downloadedWishList = modifiedWishList
        } else {
            _errorState.emit(Error("Failed to upload user data"))
        }
    }

    private suspend fun downloadMine(): WishList? {
        return withContext(Dispatchers.IO) {
            _compositeRepository.downloadMine()?.also {
                downloadedWishList = it
            }
        }
    }

    private suspend fun download(login: String): WishList? {
        return withContext(Dispatchers.IO) {
            _compositeRepository.download(login)?.also {
                downloadedWishList = it
            }
        }
    }

    private val _currentScreenState: MutableStateFlow<Screen> =
        MutableStateFlow(WishListScreen(null))
    val currentScreenState: StateFlow<Screen> = _currentScreenState
        .stateIn(viewModelScope, SharingStarted.Eagerly, WishListScreen(null))

    suspend fun onClickUsers() {
        openUsersScreen()
    }

    suspend fun onClickWishList() {
        openWishListScreen()
    }

    suspend fun onClickNewWish() {
        openNewWishScreen()
    }

    suspend fun onClickSaveNewWish(oldWish: Wish?, newWish: Wish) {
        modifyWish(oldWish, newWish)
        openWishListScreen()
    }

    suspend fun onClickWish(wish: Wish) {
        openWishScreen(wish)
    }

    suspend fun onClickEditWish(wish: Wish) {
        openEditWishScreen(wish)
    }

    suspend fun onClickDeleteWish(wish: Wish) {
        openDeleteWishScreen(wish)
    }

    suspend fun onClickConfirmDeleteWish(wish: Wish) {
        modifyWish(wish, null)
        openWishListScreen()
    }

    suspend fun onClickUser(user: User) {
        openUserWishListScreen(user)
    }

    suspend fun onClickLogout() {
        _compositeRepository.logout()
    }

    private suspend fun openWishListScreen() {
        _currentScreenState.emit(WishListScreen(modifiedWishList))
        downloadMine()
    }

    private suspend fun openUsersScreen() {
        val userList = modifiedWishList
            ?.friends
            ?: emptyList()
        _currentScreenState.emit(UsersScreen(userList))
    }

    private fun openWishScreen(wish: Wish) {
        _currentScreenState.value = WishScreen(wish)
    }

    private fun openNewWishScreen() {
        _currentScreenState.value = NewWishScreen
    }

    private fun openEditWishScreen(wish: Wish) {
        _currentScreenState.value = EditWishScreen(wish)
    }

    private fun openDeleteWishScreen(wish: Wish) {
        _currentScreenState.value = DeleteWishScreen(wish = wish)
    }

    private suspend fun openUserWishListScreen(user: User) {
        val wishlist = TODO("load user $user wishlist")
        _currentScreenState.emit(WishListScreen(wishlist))
    }

    private fun modifyWish(
        oldWish: Wish?,
        newWish: Wish?
    ) {
        val oldWishList = modifiedWishList?.wishes ?: emptyList()
        val oldWishIndex = oldWishList.indexOf(oldWish)

        val newWishList: List<Wish> = when {
            newWish == null && oldWishIndex != INDEX_NOT_FOUND -> {
                oldWishList.toMutableList().apply {
                    removeAt(oldWishIndex)
                }
            }

            newWish != null && oldWishIndex != INDEX_NOT_FOUND -> {
                oldWishList.toMutableList().apply {
                    removeAt(oldWishIndex)
                    add(oldWishIndex, newWish)
                }
            }

            newWish != null && oldWishIndex == INDEX_NOT_FOUND -> {
                oldWishList + newWish
            }

            else -> oldWishList
        }

        modifiedWishList = WishList(
            friends = modifiedWishList?.friends ?: emptyList(),
            wishes = newWishList,
            promises = modifiedWishList?.promises ?: emptyMap()
        )
    }

    private suspend fun cleanUp() {
        downloadedWishList = null
        _currentUserState.emit(null)
        _errorState.emit(null)
    }

    companion object {
        private const val INDEX_NOT_FOUND = -1
    }
}