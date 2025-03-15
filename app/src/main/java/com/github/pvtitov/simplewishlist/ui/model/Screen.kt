package com.github.pvtitov.simplewishlist.ui.model

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.domain.model.WishList

sealed interface Screen

data object LoginScreen : Screen

data class UsersScreen(val users: List<User>) : Screen

data class WishListScreen(val wishList: WishList?) : Screen

data class WishScreen(val wish: Wish) : Screen

data object NewWishScreen : Screen

data class EditWishScreen(val wish: Wish) : Screen

data class DeleteWishScreen(val wish: Wish) : Screen

data class PromiseWishScreen(val wish: Wish) : Screen