package com.github.pvtitov.simplewishlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.pvtitov.simplewishlist.data.CompositeRepository
import com.github.pvtitov.simplewishlist.ui.model.Friend
import com.github.pvtitov.simplewishlist.ui.model.Profile
import com.github.pvtitov.simplewishlist.ui.model.Wish
import com.github.pvtitov.simplewishlist.utils.DI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AwlViewModel(
    private val dispatcherIo: CoroutineDispatcher,
    private val repository: CompositeRepository
) : ViewModel() {

    private val defaultContext = dispatcherIo + CoroutineExceptionHandler { _, e ->
        Log.e("AwlViewModel", "error in coroutine: $e", e)
        _isLoadingFlow.value = false
    }
    private val _profileFlow = MutableStateFlow<Profile?>(null)

    val isAuthorizedFlow: StateFlow<Boolean> = _profileFlow.map { it != null }
        .stateIn(viewModelScope, SharingStarted.Lazily, _profileFlow.value != null)

    val avatarUrlFlow: StateFlow<String?> = _profileFlow.map { it?.avatarUrl }
        .stateIn(viewModelScope, SharingStarted.Lazily, _profileFlow.value?.avatarUrl)

    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val _myWishListFlow = MutableStateFlow<List<Wish>>(emptyList())
    val myWishListFlow = _myWishListFlow.asStateFlow()

    private val _friendListFlow = MutableStateFlow<List<Friend>>(emptyList())
    val friendListFlow = _friendListFlow.asStateFlow()

    private val _wishListByFriendFlow = MutableStateFlow<Map<Friend, List<Wish>>>(emptyMap())
    val wishListByFriendFlow = _wishListByFriendFlow.asStateFlow()

    fun logout() {
        viewModelScope.launch(defaultContext) {
            repository.logout()
            _profileFlow.value = null
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch(defaultContext) {
            _isLoadingFlow.value = true
            coroutineScope {
                repository.getMyLogin()?.let { login ->
                    // TODO get user avator url
                    _profileFlow.value = Profile(
                        avatarUrl = null
                    )
                }

                val myData = repository.downloadMine()

                myData?.let { wishList ->
                    wishList.wishes.map { w ->
                        Wish(
                            title = w.title,
                            description = w.description,
                            link = w.wishUrl,
                            imageLink = w.imageUrl
                        )
                    }
                }?.let {
                    _myWishListFlow.value = it
                }

                myData?.let { wishlist ->
                    wishlist.friends
                        .map { f ->
                            Friend(
                                name = f.name ?: "",
                                login = f.login,
                                wishesCount = f.wishList?.wishes?.size ?: 0,
                                imageLink = f.imageUrl
                            )
                        }
                }?.let {
                    _friendListFlow.value = it
                }
            }
            _isLoadingFlow.value = false
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AwlViewModel(
                    dispatcherIo = Dispatchers.IO,
                    repository = DI.compositeRepository
                )
            }
        }
    }
}