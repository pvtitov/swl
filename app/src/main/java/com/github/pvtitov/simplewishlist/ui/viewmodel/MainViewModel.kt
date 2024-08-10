package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.ui.model.UIError
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.model.UsersScreen
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

    // Set up manual data source
    private lateinit var _repository: Repository

    fun setManualAccountDataSource(repository: Repository) {
        this._repository = repository
    }

    // Login
    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val currentLoginFlow: StateFlow<String?> = _credentialsState
        .map { it?.login }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onLogin(credentials: Credentials?) {
        viewModelScope.launch(ioDispatcher) {
            cleanUp()
            _credentialsState.emit(credentials)
            _currentScreenState.emit(UsersScreen(getUsers()))
        }
        viewModelScope.launch(ioDispatcher) {
            _downloadedDataStateFlow.collect {
                updateScreen()
            }
        }
    }

    // Error state
    private val _errorState = MutableStateFlow(NO_ERROR)
    val errorState: StateFlow<UIError> = _errorState.asStateFlow()

    // Data transfer
    private val _downloadedDataStateFlow = MutableStateFlow<Dto?>(null)
    private val _updatedDataStateFlow = MutableStateFlow<Dto?>(null)

    // TODO check if can collect one emition from MutableStateFlow multiple times (multiple subscribes)
    val isDataUpdatedState: Flow<Boolean> = combine(
        _downloadedDataStateFlow,
        _updatedDataStateFlow
    ) { downloadedData, updatedData ->
        updatedData != null && updatedData != downloadedData
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private suspend fun updateScreen() {
        val newData = _downloadedDataStateFlow.value
        val newState = when (val oldData = _currentScreenState.value) {
            is UsersScreen -> UsersScreen(getUsers())
            is WishlistScreen ->
                WishlistScreen(newData?.data?.find { it.user == oldData.userData?.user })
            else -> oldData
        }
        _currentScreenState.emit(newState)
    }

    private fun upload() {
        val data = _updatedDataStateFlow.value ?: return
        viewModelScope.launch(ioDispatcher) {
            val isUploaded = withContext(Dispatchers.Main) {
                _repository.export(data)
            }
            if (!isUploaded) {
                _errorState.emit(UIError("Failed to upload user data"))
            }
        }
    }

    private fun download() {
        viewModelScope.launch(ioDispatcher) {
            val data = withContext(Dispatchers.Main) {
                _repository.import()
            }
            if (data != null) {
                _downloadedDataStateFlow.emit(data)
                _updatedDataStateFlow.emit(null)
            } else {
                _errorState.emit(UIError("Failed to download user data"))
            }
        }
    }

    // UI state
    private val _currentScreenState: MutableStateFlow<Screen> =
        MutableStateFlow(LoginScreen)
    val currentScreenState: StateFlow<Screen> = _currentScreenState
        .stateIn(viewModelScope, SharingStarted.Eagerly, LoginScreen)

    // UI callbacks
    fun onClickUsers() {
        viewModelScope.launch(ioDispatcher) {
            _currentScreenState.emit(
                UsersScreen(getUsers())
            )
        }
    }

    fun onClickLogin() {
        viewModelScope.launch(ioDispatcher) {
            _currentScreenState.emit(
                LoginScreen
            )
        }
    }

    fun onClickDownload() {
        viewModelScope.launch(ioDispatcher) {
            download()
        }
    }

    fun onClickUpload() {
        viewModelScope.launch(ioDispatcher) {
            upload()
        }
    }

    fun onClickNewWish() {
        // TODO
    }

    fun onClickWish(wish: Wish) {
        // TODO
    }

    fun onClickUser(user: User) {
        viewModelScope.launch(ioDispatcher) {
            val wishlist = _downloadedDataStateFlow.value
                ?.data
                ?.find { it.user == user }
            _currentScreenState.emit(
                WishlistScreen(wishlist)
            )
        }
    }

    private fun getUsers(): List<User> {
        return _downloadedDataStateFlow.value
            ?.data
            ?.map { it.user }
            ?: emptyList()
    }

    private suspend fun cleanUp() {
        _downloadedDataStateFlow.emit(null)
        _updatedDataStateFlow.emit(null)
        _currentScreenState.emit(LoginScreen)
        _credentialsState.emit(null)
        _errorState.emit(NO_ERROR)
    }

    companion object {
        val NO_ERROR = UIError("")
    }
}