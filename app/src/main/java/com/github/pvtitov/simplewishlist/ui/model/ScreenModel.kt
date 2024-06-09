package com.github.pvtitov.simplewishlist.ui.model

import com.github.pvtitov.simplewishlist.domain.model.Wish

sealed interface ScreenModel

data class WishlistScreenModel(val wishlist: List<Wish>): ScreenModel

data class WishScreenModel(val wish: Wish): ScreenModel