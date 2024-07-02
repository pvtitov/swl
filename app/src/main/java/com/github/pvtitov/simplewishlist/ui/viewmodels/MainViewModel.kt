package com.github.pvtitov.simplewishlist.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.ui.model.UIError
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.model.UsersScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private lateinit var _manualRepository: Repository

    private val _currentScreenState = MutableStateFlow(EMPTY_USERS)
    val currentScreenState: StateFlow<ScreenModel> = _currentScreenState.asStateFlow()

    private val _userDataState = MutableStateFlow(NO_DATA)
    val userDataState: StateFlow<Dto> = _userDataState.asStateFlow()

    private val _errorState = MutableStateFlow(NO_ERROR)
    val errorState: StateFlow<UIError> = _errorState.asStateFlow()

    fun setManualAccountDataSource(manualRepository: Repository) {
        this._manualRepository = manualRepository
    }

    fun upload() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUploaded = withContext(Dispatchers.Main) {
                _manualRepository.export(_userDataState.value)
            }
            if (!isUploaded) {
                _errorState.emit(UIError("Failed to upload user data"))
            }
        }
    }

    fun download() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = withContext(Dispatchers.Main) {
                _manualRepository.import()
            }
            if (data != null) {
                _userDataState.emit(data)
            } else {
                _errorState.emit(UIError("Failed to download user data"))
            }
        }
    }

    companion object {
        val NO_DATA = Dto(emptyList(), User("", ""))
        val NO_ERROR = UIError("")
        val EMPTY_USERS = UsersScreenModel(emptyList())
    }
}