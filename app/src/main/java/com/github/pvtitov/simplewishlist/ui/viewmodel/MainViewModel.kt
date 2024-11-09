package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
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
import kotlinx.coroutines.flow.combine
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

    private val _downloadedDataStateFlow = MutableStateFlow<Dto?>(null)
    private val _modifiedDataStateFlow = MutableStateFlow<Dto?>(null)

    val isDataUpdatedState: Flow<Boolean> = combine(
        _downloadedDataStateFlow,
        _modifiedDataStateFlow
    ) { downloadedData, updatedData ->
        updatedData != null && updatedData != downloadedData
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun upload() {
        val data = _modifiedDataStateFlow.value ?: return
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
            _downloadedDataStateFlow.emit(data)
            _modifiedDataStateFlow.emit(null)
        }
        return data
    }

    private fun downloadFriend() {
        viewModelScope.launch(ioDispatcher) {
            val newData = withContext(Dispatchers.Main) {
                _manualRepository.import()
            }
            val oldData = _modifiedDataStateFlow.value
                ?: _downloadedDataStateFlow.value
            when {
                oldData == null -> _errorState.emit(Error("Should load your user data first"))
                newData == null -> _errorState.emit(Error("Failed to download friend's user data"))
                else -> {
                    val oldDataList = oldData.data
                    val resultData = Dto(
                        oldDataList + newData.data.filterNot { oldDataList.contains(it) },
                        oldData.sender
                    )
                    _modifiedDataStateFlow.emit(resultData)
                    onClickUsers()
                }
            }
        }
    }

    private val _currentScreenState: MutableStateFlow<Screen> =
        MutableStateFlow(LoginScreen)
    val currentScreenState: StateFlow<Screen> = _currentScreenState
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoginScreen)

    fun onLogin(credentials: Credentials?) {
        viewModelScope.launch(ioDispatcher) {
            cleanUp()
            if (credentials?.isVerified() == true) {
                _credentialsState.emit(credentials)
                onClickUsers()
            }
        }
    }

    private fun requireAuthorization(action: () -> Unit) {
        if (_credentialsState.value != null) {
            action()
        }
    }

    fun onClickUsers() {
        requireAuthorization {
            viewModelScope.launch(ioDispatcher) {
                (getCurrentData() ?: download())
                    ?.data
                    ?.map { it.user }
                    ?.let { _currentScreenState.emit(UsersScreen(it)) }
            }
        }
    }

    fun onClickAddUser() {
        requireAuthorization {
            downloadFriend()
        }
    }

    fun onClickLogin() {
        requireAuthorization {
            _currentScreenState.value = LoginScreen
        }
    }

    fun onClickDownload() {
        requireAuthorization {
            viewModelScope.launch(ioDispatcher) {
                download()
                onClickUsers()
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
            _currentScreenState.value = NewWishScreen
        }
    }

    fun onClickWish(wish: Wish) {
        requireAuthorization {
            _currentScreenState.value = WishScreen(wish)
        }
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

    private fun getCurrentData(): Dto? =
        _modifiedDataStateFlow.value ?: _downloadedDataStateFlow.value

    private suspend fun cleanUp() {
        _downloadedDataStateFlow.emit(null)
        _modifiedDataStateFlow.emit(null)
        _currentScreenState.emit(LoginScreen)
        _credentialsState.emit(null)
        _errorState.emit(null)
    }

    companion object {
        val TAG = MainViewModel::class.simpleName
    }
}