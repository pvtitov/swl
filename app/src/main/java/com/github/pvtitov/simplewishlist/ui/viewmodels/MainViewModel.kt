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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private lateinit var _manualRepository: Repository

    private val _userDataState = MutableStateFlow(NO_DATA)
    val userDataState: StateFlow<Dto> = _userDataState.asStateFlow()

    private val _errorState = MutableStateFlow(NO_ERROR)
    val errorState: StateFlow<UIError> = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            val wish1 = Wish("wish_01", "description_01", wishUrl = "http://wish_01.net")
            val wish2 = Wish("wish_02", "description_02", wishUrl = "http://wish_02.net")
            val wish3 = Wish("wish_03", "description_03", wishUrl = "http://wish_03.net")
            val user = getMyLogin()
            _userDataState.emit(
                Dto(
                    listOf(
                        UserData(
                            user,
                            listOf(wish1, wish2, wish3),
                            emptyMap(),
                            0L,
                            0
                        )
                    ),
                    user
                )

            )
        }
    }

    fun setManualAccountDataSource(manualRepository: Repository) {
        this._manualRepository = manualRepository
    }

    fun upload() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUploaded = withContext(Dispatchers.Main) {
                _manualRepository.export(getMyCurrentUserData())
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

    fun onFileOpened(uri: Uri) {
        // TODO open file by uri
        _userDataState.value = NO_DATA
    }

    fun onFileSaved(uri: Uri) {
        // TODO do something
    }

    fun getMyLogin(): User {
        return User("test_login", "test_name") //TODO
    }

    private fun getMyCurrentUserData(): Dto {
        return userDataState.value
    }

    companion object {
        val NO_DATA = Dto(emptyList(), User("", ""))
        val NO_ERROR = UIError("")
    }
}