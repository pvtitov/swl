package com.github.pvtitov.simplewishlist.ui.model

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

sealed interface ScreenModel

object LoginScreenModel: ScreenModel

data class UsersScreenModel(val users: List<User>): ScreenModel

data class WishlistScreenModel(val wishlist: List<Wish>): ScreenModel

data class WishScreenModel(val wish: Wish): ScreenModel