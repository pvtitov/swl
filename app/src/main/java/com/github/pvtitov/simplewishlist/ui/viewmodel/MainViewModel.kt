package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Repository
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private lateinit var _manualRepository: Repository<WishList>

    fun setManualAccountDataSource(repository: Repository<WishList>) {
        this._manualRepository = repository
    }

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
    val isWishListUpdatedState: Flow<Boolean> = _isWishListUpdatedState.asStateFlow()

    private fun upload() {
        val data = modifiedWishList ?: return
        viewModelScope.launch(ioDispatcher) {
            val isUploaded = withContext(Dispatchers.Main) {
                _manualRepository.upload(data)
            }
            if (isUploaded) {
                downloadedWishList = modifiedWishList
            } else {
                _errorState.emit(Error("Failed to upload user data"))
            }
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

    private fun requireAuthorization(action: (Credentials) -> Unit) {
        val credentials = _credentialsState.value
        if (credentials != null) {
            action(credentials)
        }
    }

    fun onClickSubmitLogin(credentials: Credentials?) {
        viewModelScope.launch(ioDispatcher) {
            cleanUp()
            if (credentials?.isVerified() == true) {
                _credentialsState.emit(credentials)
                openWishListScreen()
            }
        }
    }

    fun onClickUsers() {
        requireAuthorization {
            openUsersScreen()
        }
    }

    fun onClickWishList() {
        requireAuthorization {
            viewModelScope.launch(ioDispatcher) {
                openWishListScreen()
            }
        }
    }

    fun onClickLogin() {
        requireAuthorization {
            openLoginScreen()
        }
    }

    fun onClickDownload() {
        requireAuthorization {
            viewModelScope.launch(ioDispatcher) {
                download()
                openWishListScreen()
            }
        }
    }

    fun onClickUpload() {
        requireAuthorization {
            upload()
        }
    }

    fun onClickNewWish() {
        requireAuthorization {
            openNewWishScreen()
        }
    }

    fun onClickSaveNewWish(oldWish: Wish?, newWish: Wish) {
        requireAuthorization {
            modifyWish(oldWish, newWish)
        }
        openWishListScreen()
    }

    fun onClickWish(wish: Wish) {
        requireAuthorization {
            openWishScreen(wish)
        }
    }

    fun onClickEditWish(wish: Wish) {
        requireAuthorization {
            openEditWishScreen(wish)
        }
    }

    fun onClickDeleteWish(wish: Wish) {
        requireAuthorization {
            openDeleteWishScreen(wish)
        }
    }

    fun onClickConfirmDeleteWish(wish: Wish) {
        requireAuthorization {
            modifyWish(wish, null)
        }
        openWishListScreen()
    }

    fun onClickUser(user: User) {
        viewModelScope.launch(ioDispatcher) {
            val wishlist = TODO("load user $user wishlist")
            _currentScreenState.emit(WishListScreen(wishlist))
        }
    }

    private fun openLoginScreen() {
        _currentScreenState.value = LoginScreen
    }

    private fun openWishListScreen() {
        viewModelScope.launch(ioDispatcher) {
            _currentScreenState.emit(WishListScreen(modifiedWishList))
        }
    }

    private fun openUsersScreen() {
        viewModelScope.launch(ioDispatcher) {
            val userList = modifiedWishList
                ?.friends
                ?: emptyList()
            _currentScreenState.emit(UsersScreen(userList))
        }
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