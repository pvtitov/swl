package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.ui.model.UIError
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
    private lateinit var _manualRepository: Repository

    fun setManualAccountDataSource(manualRepository: Repository) {
        this._manualRepository = manualRepository
    }

    // Login
    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val credentialsState: StateFlow<Credentials?> = _credentialsState.asStateFlow()

    fun onLogin(credentials: Credentials?) {
        cleanUp()
        viewModelScope.launch(Dispatchers.IO) {
            _credentialsState.emit(credentials)
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
                _manualRepository.export(data)
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
                _manualRepository.import()
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
    private val _currentScreenState = MutableStateFlow(EMPTY_USERS)
    val currentScreenState: StateFlow<ScreenModel> = _currentScreenState.asStateFlow()

    fun onOpenUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentScreenState.emit(
                UsersScreenModel(getUsers())
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
            _currentScreenState.emit(EMPTY_USERS)
            _credentialsState.emit(null)
            _errorState.emit(NO_ERROR)
        }
    }

    companion object {
        val NO_ERROR = UIError("")
        val EMPTY_USERS = UsersScreenModel(emptyList())
    }
}