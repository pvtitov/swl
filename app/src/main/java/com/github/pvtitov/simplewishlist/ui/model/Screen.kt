package com.github.pvtitov.simplewishlist.ui.model

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import com.github.pvtitov.simplewishlist.domain.model.Wish

sealed interface Screen

data object LoginScreen : Screen

data class UsersScreen(val users: List<User>) : Screen

data class WishlistScreen(val userData: UserData?) : Screen

data class WishScreen(val wish: Wish) : Screen

data object NewWishScreen : Screen

data class EditWishScreen(val wish: Wish) : Screen

data class DeleteWishScreen(val wish: Wish) : Screen

data class PromiseWishScreen(val wish: Wish) : Screen