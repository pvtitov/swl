package com.github.pvtitov.simplewishlist.ui.viewmodel

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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

typealias DomainWish = com.github.pvtitov.simplewishlist.domain.model.Wish

class AwlViewModel(
    private val interactor: Interactor,
    dispatcherIo: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val logger = DI.logger

    private val defaultContext = dispatcherIo + CoroutineExceptionHandler { _, e ->
        logger.error(TAG, "error in coroutine: $e", e)
        _isLoadingFlow.value = false
    }
    private val _profileFlow = MutableStateFlow<Profile?>(null)

    val isAuthorizedFlow: StateFlow<Boolean> = _profileFlow
        .map { it != null }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _profileFlow.value != null
        )

    val avatarUrlFlow: StateFlow<String?> = _profileFlow
        .map { it?.avatarUrl }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _profileFlow.value?.avatarUrl
        )

    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val _myWishListFlow = MutableStateFlow<List<Wish>>(emptyList())
    val myWishListFlow = _myWishListFlow.asStateFlow()

    private val _friendListFlow = MutableStateFlow<List<Friend>>(emptyList())
    val friendListFlow = _friendListFlow.asStateFlow()

    private val _wishListByFriendFlow = MutableStateFlow<List<List<Wish>>>(emptyList())
    val wishListByFriendFlow = _wishListByFriendFlow.asStateFlow()

    fun logout() {
        logger.debug(TAG, "logout()")
        _isLoadingFlow.value = true
        viewModelScope.launch(defaultContext) {
            interactor.logout()
            _profileFlow.value = null
            _isLoadingFlow.value = false
        }
    }

    fun loginWithGoogle() {
        logger.debug(TAG, "loginWithGoogle()")
        _isLoadingFlow.value = true
        viewModelScope.launch(defaultContext) {
            val result = interactor.login()
            val user = (result as? Status.Success)?.result
            if (user != null) {
                _profileFlow.value = Profile(
                    // TODO get user avator url
                    avatarUrl = null
                )
            }
            _isLoadingFlow.value = false
        }
    }

    fun loadMyWishList() {
        logger.debug(TAG, "loadMyWishList()")
        _isLoadingFlow.value = true
        viewModelScope.launch {
            isAuthorizedFlow
                .filter { isAuthorized -> isAuthorized }
                .collect {
                    val myWishList = (interactor.getMyWishList() as? Status.Success)?.result

                    val wishList = myWishList?.map { w ->
                        Wish(
                            title = w.title,
                            description = w.description,
                            link = w.url,
                            imageLink = w.imageUrl
                        )
                    } ?: emptyList()
                    _myWishListFlow.value = wishList
                    _isLoadingFlow.value = false
                }

            delay(TIMEOUT)
            if (!isAuthorizedFlow.value) {
                joinAll()
            }
        }
    }

    private suspend fun loadAllData() {
        logger.debug(TAG, "loadAllData()")
        coroutineScope {
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
        logger.debug(TAG, "addWish(), wish = $wish")
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
        logger.debug(TAG, "saveFriend(), name = $name, login = $login")
        viewModelScope.launch(defaultContext) {
            interactor.addFriend(User(id = "", name = name ?: login))
            loadAllData()
        }
    }

    fun editWish(original: Wish, modified: Wish) {
        logger.debug(TAG, "editWish(), original wish = $original, modified wish = $modified")
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
        logger.debug(TAG, "deleteWish(), wish = $wish")
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
                    interactor = DI.interactor
                )
            }
        }

        private const val TAG = "AwlViewModel"
        private const val TIMEOUT = 3000L
    }
}