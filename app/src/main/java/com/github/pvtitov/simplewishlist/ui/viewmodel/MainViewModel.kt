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
        .map { it?.login }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _errorState = MutableStateFlow<Error?>(null)
    val errorState: StateFlow<Error?> = _errorState.asStateFlow()

    private var myWishList: WishList? = null

    private suspend fun downloadMine(): WishList? {
        return withContext(Dispatchers.IO) {
            _compositeRepository.getMyLogin()?.let { login -> _currentUserState.value = User(login = login) }
            _compositeRepository.downloadMine()?.also {
                myWishList = it
            }
        }
    }

    private suspend fun download(login: String): WishList? {
        return withContext(Dispatchers.IO) {
            _compositeRepository.download(login)?.also {
                myWishList = it
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

    suspend fun onClickAddFriend() {
        openAddFriendScreen()
    }

    suspend fun onClickSaveNewFriend(newFriend: User) {
        addFriendLocally(newFriend)
        if (_compositeRepository.addFriend(newFriend.login)) {
            _compositeRepository
            openUsersScreen()
        }
    }

    suspend fun onClickWishList() {
        openWishListScreen()
    }

    suspend fun onClickNewWish() {
        openNewWishScreen()
    }

    suspend fun onClickSaveNewWish(oldWish: Wish?, newWish: Wish) {
        createEditOrDeleteWish(oldWish, newWish)
        myWishList?.let { _compositeRepository.upload(it) }
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
        createEditOrDeleteWish(wish, null)
        myWishList?.let { _compositeRepository.upload(it) }
        openWishListScreen()
    }

    suspend fun onClickUser(user: User) {
        openUserWishListScreen(user)
    }

    suspend fun onClickLogout() {
        _compositeRepository.logout()
        _currentUserState.value = null
    }

    private suspend fun openWishListScreen() {
        downloadMine()
        _currentScreenState.value = WishListScreen(myWishList)
    }

    private suspend fun openUsersScreen() {
        downloadMine()
        _currentScreenState.value = UsersScreen(myWishList?.friends ?: emptyList())
    }

    private fun openAddFriendScreen() {
        _currentScreenState.value = AddFriendScreen
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
        val wishlist = download(user.login)
        _currentScreenState.value = WishListScreen(wishlist)
    }

    private fun createEditOrDeleteWish(
        oldWish: Wish?,
        newWish: Wish?
    ) {
        val oldWishList = myWishList?.wishes ?: emptyList()
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

        myWishList = WishList(
            friends = myWishList?.friends ?: emptyList(),
            wishes = newWishList,
            promises = myWishList?.promises ?: emptyMap()
        )
    }

    private fun addFriendLocally(
        newFriend: User
    ) {
        val oldFriendsList = myWishList?.friends ?: emptyList()

        val newWishList: List<User> = oldFriendsList + newFriend

        myWishList = WishList(
            friends = newWishList,
            wishes = myWishList?.wishes ?: emptyList(),
            promises = myWishList?.promises ?: emptyMap()
        )
    }

    private suspend fun cleanUp() {
        myWishList = null
        _currentUserState.value = null
        _errorState.value = null
    }

    companion object {
        private const val INDEX_NOT_FOUND = -1
    }
}