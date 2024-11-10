package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.model.DeleteWishScreen
import com.github.pvtitov.simplewishlist.ui.model.EditWishScreen
import com.github.pvtitov.simplewishlist.ui.model.Error
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.ui.model.NewWishScreen
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.model.UsersScreen
import com.github.pvtitov.simplewishlist.ui.model.WishScreen
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreen
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
import org.jetbrains.annotations.TestOnly

class MainViewModel : ViewModel() {

    private var ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    @TestOnly
    fun setIoDispatcher(dispatcher: CoroutineDispatcher) {
        ioDispatcher = dispatcher
    }

    private lateinit var _manualRepository: Repository

    fun setManualAccountDataSource(repository: Repository) {
        this._manualRepository = repository
    }

    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val currentLoginFlow: StateFlow<String?> = _credentialsState
        .map { it?.login }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private suspend fun Credentials.isVerified(): Boolean {
        return login.isNotEmpty()
    }

    private val _errorState = MutableStateFlow<Error?>(null)
    val errorState: StateFlow<Error?> = _errorState.asStateFlow()

    private var downloadedData: Dto? = null
        set(value) {
            field = value
        }
    private var modifiedData: Dto? = null
        set(value) {
            field = value
            _isDataUpdatedState.value = value != null && value != downloadedData
        }

    private val _isDataUpdatedState = MutableStateFlow(false)
    val isDataUpdatedState: Flow<Boolean> = _isDataUpdatedState.asStateFlow()

    private fun upload() {
        val data = modifiedData ?: return
        viewModelScope.launch(ioDispatcher) {
            val isUploaded = withContext(Dispatchers.Main) {
                _manualRepository.export(data)
            }
            if (!isUploaded) {
                _errorState.emit(Error("Failed to upload user data"))
            }
        }
    }

    private suspend fun download(): Dto? {
        val data = withContext(Dispatchers.Main) {
            _manualRepository.import()
        }
        if (data != null) {
            downloadedData = data
            modifiedData = data
        }
        return data
    }

    private suspend fun downloadFriend(): Dto? {
        val newData = withContext(Dispatchers.Main) {
            _manualRepository.import()
        }
        val oldData = getCurrentData()
        return when {
            oldData == null -> null
            newData == null -> oldData
            else -> {
                val oldDataList = oldData.data
                val resultData = Dto(
                    oldDataList + newData.data.filterNot { oldDataList.contains(it) },
                    oldData.sender
                )
                modifiedData = resultData

                resultData
            }
        }
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
                openUsersScreen()
            }
        }
    }

    fun onClickUsers() {
        requireAuthorization {
            openUsersScreen()
        }
    }

    fun onClickAddUser() {
        requireAuthorization {
            viewModelScope.launch(ioDispatcher) {
                downloadFriend()
                openUsersScreen()
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
                openUsersScreen()
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
        requireAuthorization { credentials ->
            modifyWish(credentials, oldWish, newWish)
        }
        openUsersScreen()
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
        requireAuthorization { credentials ->
            modifyWish(credentials, wish, null)
        }
        openUsersScreen()
    }

    fun onClickUser(user: User) {
        viewModelScope.launch(ioDispatcher) {
            val wishlist = getCurrentData()
                ?.data
                ?.find { it.user == user }
            _currentScreenState.emit(
                WishlistScreen(wishlist)
            )
        }
    }

    private fun openLoginScreen() {
        _currentScreenState.value = LoginScreen
    }

    private fun openUsersScreen() {
        viewModelScope.launch(ioDispatcher) {
            val userList = getCurrentData()
                ?.data
                ?.map { it.user }
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
        credentials: Credentials,
        oldWish: Wish?,
        newWish: Wish?
    ) {
        val dto = getCurrentData() ?: return
        val userDataList = dto.data
        var currentUserDataMutable: UserData? = null
        var currentUserDataIndexMutable: Int? = null
        userDataList.forEachIndexed { index, userData ->
            if (userData.user.login == credentials.login) {
                currentUserDataMutable = userData
                currentUserDataIndexMutable = index
                return@forEachIndexed
            }
        }
        val currentUserData = currentUserDataMutable ?: return
        val currentUserDataIndex = currentUserDataIndexMutable ?: return

        var oldWishIndexMutable: Int? = null
        val oldWishList = currentUserData.wishList
        oldWishList.forEachIndexed { index, wish ->
            if (wish == oldWish) {
                oldWishIndexMutable = index
                return@forEachIndexed
            }
        }
        val oldWishIndex = oldWishIndexMutable

        val newWishList: List<Wish> = when {
            newWish == null && oldWishIndex != null -> {
                oldWishList.toMutableList().apply {
                    removeAt(oldWishIndex)
                }
            }

            newWish != null && oldWishIndex != null -> {
                oldWishList.toMutableList().apply {
                    removeAt(oldWishIndex)
                    add(oldWishIndex, newWish)
                }
            }

            newWish != null && oldWishIndex == null -> {
                oldWishList + newWish
            }

            else -> oldWishList
        }

        val newUserData = UserData(
            user = currentUserData.user,
            wishList = newWishList,
            promises = currentUserData.promises,
            date = currentUserData.date,
            checkSum = currentUserData.checkSum
        )

        val newUserDataList: List<UserData> = userDataList.toMutableList().apply {
            removeAt(currentUserDataIndex)
            add(
                currentUserDataIndex,
                newUserData
            )
        }
        val newDto = Dto(newUserDataList, dto.sender)
        modifiedData = newDto
    }

    private fun getCurrentData(): Dto? =
        modifiedData ?: downloadedData

    private suspend fun cleanUp() {
        modifiedData = null
        downloadedData = null
        _credentialsState.emit(null)
        _errorState.emit(null)
    }
}