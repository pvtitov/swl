package com.github.pvtitov.simplewishlist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _credentialsState: MutableStateFlow<Credentials?> = MutableStateFlow(null)
    val credentialsState: StateFlow<Credentials?> = _credentialsState.asStateFlow()

    fun onLogin(credentials: Credentials?) {
        viewModelScope.launch(Dispatchers.IO) {
            _credentialsState.emit(credentials)
        }
    }
}