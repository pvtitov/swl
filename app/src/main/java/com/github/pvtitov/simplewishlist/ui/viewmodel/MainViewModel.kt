package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.ui.model.UIError
import com.github.pvtitov.simplewishlist.ui.model.LoginScreenModel
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.model.UsersScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    // Set up manual data source
    private lateinit var _repository: Repository

    fun setManualAccountDataSource(repository: Repository) {
        this._repository = repository
    }

    // Login
    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val credentialsState: StateFlow<Credentials?> = _credentialsState.asStateFlow()

    fun onLogin(credentials: Credentials?) {
        cleanUp()
        viewModelScope.launch(Dispatchers.IO) {
            _credentialsState.emit(credentials)
            _currentScreenState.emit(UsersScreenModel(getUsers()))
        }
    }

    // Error state
    private val _errorState = MutableStateFlow(NO_ERROR)
    val errorState: StateFlow<UIError> = _errorState.asStateFlow()

    // Data transfer
    private var downloadedData: Dto? = null
    private var dataToUpload: Dto? = null

    private val _isDataUpdatedState = MutableStateFlow(false)
    val isDataUpdatedState: StateFlow<Boolean> = _isDataUpdatedState.asStateFlow()

    fun upload() {
        val data = dataToUpload ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val isUploaded = withContext(Dispatchers.Main) {
                _repository.export(data)
            }
            if (!isUploaded) {
                _errorState.emit(UIError("Failed to upload user data"))
            }
            onDataUpdated()
        }
    }

    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = withContext(Dispatchers.Main) {
                _repository.import()
            }
            if (data != null) {
                downloadedData = data
                dataToUpload = data
            } else {
                _errorState.emit(UIError("Failed to download user data"))
            }
            onDataUpdated()
        }
    }

    private fun onDataUpdated() {
        viewModelScope.launch(Dispatchers.IO) {
            _isDataUpdatedState.emit(downloadedData != dataToUpload)
        }
    }

    // UI state
    private val _currentScreenState: MutableStateFlow<ScreenModel> =
        MutableStateFlow(LoginScreenModel)
    val currentScreenState: StateFlow<ScreenModel> = _currentScreenState.asStateFlow()

    // UI callbacks
    fun onOpenUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentScreenState.emit(
                UsersScreenModel(getUsers())
            )
        }
    }

    fun onOpenLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentScreenState.emit(
                LoginScreenModel
            )
        }
    }

    private fun getUsers(): List<User> {
        return downloadedData?.data?.map { it.user } ?: emptyList()
    }

    private fun cleanUp() {
        downloadedData = null
        dataToUpload = null
        viewModelScope.launch(Dispatchers.IO) {
            _currentScreenState.emit(LoginScreenModel)
            _credentialsState.emit(null)
            _errorState.emit(NO_ERROR)
        }
    }

    companion object {
        val NO_ERROR = UIError("")
    }
}