package com.github.pvtitov.simplewishlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.model.Status
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.model.Friend
import com.github.pvtitov.simplewishlist.ui.model.Profile
import com.github.pvtitov.simplewishlist.ui.model.Wish
import com.github.pvtitov.simplewishlist.utils.DI
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

typealias DomainWish = com.github.pvtitov.simplewishlist.domain.model.Wish

class AwlViewModel(
    private val dispatcherIo: CoroutineDispatcher,
    private val interactor: Interactor
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

    private val _wishListByFriendFlow = MutableStateFlow<List<List<Wish>>>(emptyList())
    val wishListByFriendFlow = _wishListByFriendFlow.asStateFlow()

    fun logout() {
        viewModelScope.launch(defaultContext) {
            interactor.logout()
            _profileFlow.value = null
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch(defaultContext) {
            _isLoadingFlow.value = true
            loadAllData()
            _isLoadingFlow.value = false
        }
    }

    private suspend fun loadAllData() {
        coroutineScope {
            (interactor.login() as? Status.Success)?.result?.let { user ->
                // TODO get user avator url
                _profileFlow.value = Profile(
                    avatarUrl = null
                )
            }

            val myWishList = (interactor.getMyWishList() as? Status.Success)?.result

            val myWishListJob = launch {
                val wishList = myWishList?.map { w ->
                    Wish(
                        title = w.title,
                        description = w.description,
                        link = w.url,
                        imageLink = w.imageUrl
                    )
                } ?: emptyList()
                _myWishListFlow.value = wishList
            }

            val myFriendList = (interactor.getMyFriendList() as? Status.Success)?.result

            val myFriendListJob = launch {
                val friendList = myFriendList
                    ?.map { user ->
                        Friend(
                            name = user.name ?: "",
                            login = "",
                            wishesCount = -1,
                            imageLink = null
                        )
                    } ?: emptyList()
                _friendListFlow.value = friendList
            }

            val wishListsOfFriendsJobs = myFriendList?.mapIndexed { index, user ->
                launch {
                    val wishList = (interactor.getWishList(user) as Status.Success).result
                    val wishListUi = wishList.map { w ->
                        Wish(
                            title = w.title,
                            description = w.description,
                            link = w.url,
                            imageLink = w.imageUrl
                        )
                    }
                    val updatedWishLists = _wishListByFriendFlow.value.toMutableList()
                        .apply { add(index, wishListUi) }
                        .toList()

                    _wishListByFriendFlow.value = updatedWishLists
                }
            }

            buildList {
                add(myWishListJob)
                add(myFriendListJob)
                wishListsOfFriendsJobs?.forEach(::add)
            }.joinAll()
        }
    }

    fun addWish(wish: Wish) {
        viewModelScope.launch(defaultContext) {
            interactor.addWish(
                DomainWish(
                    title = wish.title ?: "",
                    description = wish.description,
                    imageUrl = wish.imageLink,
                    url = wish.link,
                )
            )
            loadAllData()
        }
    }

    fun saveFriend(name: String?, login: String) {
        viewModelScope.launch(defaultContext) {
            interactor.addFriend(User(id = "", name = name ?: login))
            loadAllData()
        }
    }

    fun editWish(original: Wish, modified: Wish) {
        viewModelScope.launch(defaultContext) {
            interactor.editWish(
                originalWish = with(original) {
                    DomainWish(
                        title = title ?: "",
                        description = description,
                        imageUrl = imageLink,
                        url = link,
                    )
                },
                modifiedWish = with(modified) {
                    DomainWish(
                        title = title ?: "",
                        description = description,
                        imageUrl = imageLink,
                        url = link,
                    )
                }
            )
            loadAllData()
        }
    }

    fun deleteWish(wish: Wish) {
        viewModelScope.launch(defaultContext) {
            interactor.deleteWish(
                wish = with(wish) {
                    DomainWish(
                        title = title ?: "",
                        description = description,
                        imageUrl = imageLink,
                        url = link,
                    )
                }
            )
            loadAllData()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AwlViewModel(
                    dispatcherIo = Dispatchers.IO,
                    interactor = DI.interactor
                )
            }
        }
    }
}