package com.github.pvtitov.simplewishlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.pvtitov.simplewishlist.ui.model.Profile
import com.github.pvtitov.simplewishlist.ui.model.Screen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NavViewModel(
    val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _profileFlow = MutableStateFlow<Profile?>(null)

    val isAuthorizedFlow: StateFlow<Boolean> = _profileFlow.map { it != null }
        .stateIn(viewModelScope, SharingStarted.Lazily, _profileFlow.value != null)

    val avatarUrlFlow: StateFlow<String?> = _profileFlow.map { it?.avatarUrl }
        .stateIn(viewModelScope, SharingStarted.Lazily, _profileFlow.value?.avatarUrl)

    private val _isLoadingFlow = MutableStateFlow<Boolean>(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val _currentScreenFlow = MutableStateFlow(Screen.Login)
    val currentScreenFlow = _currentScreenFlow.asStateFlow()

    fun logout() {
        // TODO actually logout with noserver
        _profileFlow.value = null
    }

    fun loginWithGoogle() {
        viewModelScope
        // TODO replace stub implementation with actual
        stubLoginWithGoogle()
    }

    fun onFiendClicked(friendIndex: Int) {

    }

    private fun stubLoginWithGoogle() {
        viewModelScope.launch(dispatcher) {
            _isLoadingFlow.value = true
            delay(1500L)
            _profileFlow.value = Profile("")
            _isLoadingFlow.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val ioDispatcher = Dispatchers.IO
                NavViewModel(dispatcher = ioDispatcher)
            }
        }
    }
}