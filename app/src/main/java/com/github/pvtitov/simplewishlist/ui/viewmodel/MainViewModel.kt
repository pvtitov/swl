package com.github.pvtitov.simplewishlist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.model.WishList
import com.github.pvtitov.simplewishlist.ui.model.DeleteWishScreen
import com.github.pvtitov.simplewishlist.ui.model.EditWishScreen
import com.github.pvtitov.simplewishlist.ui.model.Error
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.ui.model.NewWishScreen
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.model.UsersScreen
import com.github.pvtitov.simplewishlist.ui.model.WishListScreen
import com.github.pvtitov.simplewishlist.ui.model.WishScreen
import com.github.pvtitov.simplewishlist.utils.DI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    internal var ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _manualRepository by lazy { DI.manualRepository }
    private val _googleDiskRepository by lazy { DI.googleDiskRepository }

    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val currentLoginFlow: StateFlow<String?> = _credentialsState
        .map { it?.login }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private fun Credentials.isVerified(): Boolean {
        return login.isNotEmpty()
    }

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

    private suspend fun upload(context: Context) {
        val data = modifiedWishList ?: return

        val isUploaded = withContext(Dispatchers.Main) {
            //_manualRepository.upload(data)
            _googleDiskRepository.upload(context, data)
        }
        if (isUploaded) {
            downloadedWishList = modifiedWishList
        } else {
            _errorState.emit(Error("Failed to upload user data"))
        }
    }

    private suspend fun download(): WishList? {
        val data = withContext(Dispatchers.Main) {
            _manualRepository.download()
        }
        if (data != null) {
            downloadedWishList = data
        }
        return data
    }

    private val _currentScreenState: MutableStateFlow<Screen> =
        MutableStateFlow(LoginScreen)
    val currentScreenState: StateFlow<Screen> = _currentScreenState
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoginScreen)

    private suspend fun requireAuthorization(action: suspend (Credentials) -> Unit) {
        val credentials = _credentialsState.value
        if (credentials != null) {
            action(credentials)
        } else {
            error("Authorization required")
        }
    }

    suspend fun onClickSubmitLogin(credentials: Credentials?) {
        cleanUp()
        if (credentials?.isVerified() == true) {
            _credentialsState.emit(credentials)
            openWishListScreen()
        }
    }

    suspend fun onClickUsers() {
        requireAuthorization {
            openUsersScreen()
        }
    }

    suspend fun onClickWishList() {
        requireAuthorization {
            openWishListScreen()
        }
    }

    fun onClickLogin() {
        openLoginScreen()
    }

    suspend fun onClickDownload() {
        requireAuthorization {
            download()
            openWishListScreen()
        }
    }

    suspend fun onClickUpload(context: Context) {
        requireAuthorization {
            upload(context)
        }
    }

    suspend fun onClickNewWish() {
        requireAuthorization {
            openNewWishScreen()
        }
    }

    suspend fun onClickSaveNewWish(oldWish: Wish?, newWish: Wish) {
        requireAuthorization {
            modifyWish(oldWish, newWish)
        }
        openWishListScreen()
    }

    suspend fun onClickWish(wish: Wish) {
        requireAuthorization {
            openWishScreen(wish)
        }
    }

    suspend fun onClickEditWish(wish: Wish) {
        requireAuthorization {
            openEditWishScreen(wish)
        }
    }

    suspend fun onClickDeleteWish(wish: Wish) {
        requireAuthorization {
            openDeleteWishScreen(wish)
        }
    }

    suspend fun onClickConfirmDeleteWish(wish: Wish) {
        requireAuthorization {
            modifyWish(wish, null)
        }
        openWishListScreen()
    }

    suspend fun onClickUser(user: User) {
        requireAuthorization {
            openUserWishListScreen(user)
        }
    }

    private fun openLoginScreen() {
        _currentScreenState.value = LoginScreen
    }

    private suspend fun openWishListScreen() {
        _currentScreenState.emit(WishListScreen(modifiedWishList))
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
        _credentialsState.emit(null)
        _errorState.emit(null)
    }

    companion object {
        private const val INDEX_NOT_FOUND = -1
    }
}