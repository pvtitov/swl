package com.github.pvtitov.simplewishlist.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.source.AccountDataSource
import com.github.pvtitov.simplewishlist.domain.model.LoginModel
import com.github.pvtitov.simplewishlist.domain.model.UserDataModel
import com.github.pvtitov.simplewishlist.domain.model.WishModel
import com.github.pvtitov.simplewishlist.domain.ui.model.UIError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _accountDataSource: AccountDataSource? = null // Not implemented yet
    private lateinit var _manualAccountDataSource: AccountDataSource

    private val _userDataState = MutableStateFlow(NO_USER)
    val userDataState: StateFlow<UserDataModel> = _userDataState.asStateFlow()

    private val _errorState = MutableStateFlow(NO_ERROR)
    val errorState: StateFlow<UIError> = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            val wish1 = WishModel("wish_01", "description_01", wishUrl = "http://wish_01.net")
            val wish2 = WishModel("wish_02", "description_02", wishUrl = "http://wish_02.net")
            val wish3 = WishModel("wish_03", "description_03", wishUrl = "http://wish_03.net")
            _userDataState.emit(
                UserDataModel(
                    LoginModel(""),
                    "Name",
                    listOf(wish1, wish2, wish3),
                    emptyMap(),
                    emptyList()
                )
            )
        }
    }

    fun setManualAccountDataSource(manualAccountDataSource: AccountDataSource) {
        this._manualAccountDataSource = manualAccountDataSource
    }

    fun upload() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUploaded = _accountDataSource?.uploadUserData(getMyCurrentUserData())
                ?: withContext(Dispatchers.Main) {
                    _manualAccountDataSource.uploadUserData(getMyCurrentUserData())
                }
            if (!isUploaded) {
                _errorState.emit(UIError("Failed to upload user data"))
            }
        }
    }

    fun download(login: LoginModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = _accountDataSource?.downloadUserData(login)
                ?: withContext(Dispatchers.Main) {
                    _manualAccountDataSource.downloadUserData(login)
                }
            if (data != null) {
                _userDataState.emit(data)
            } else {
                _errorState.emit(UIError("Failed to download user data"))
            }
        }
    }

    fun onFileOpened(uri: Uri) {
        // TODO open file by uri
        _userDataState.value = NO_USER
    }

    fun onFileSaved(uri: Uri) {
        // TODO do something
    }

    fun getMyLogin(): LoginModel {
        return LoginModel("")
    }

    private fun getMyCurrentUserData(): UserDataModel {
        return userDataState.value
    }

    companion object {
        val NO_USER = UserDataModel(LoginModel("no user"), "", emptyList(), emptyMap(), emptyList())
        val NO_ERROR = UIError("no error")
    }
}