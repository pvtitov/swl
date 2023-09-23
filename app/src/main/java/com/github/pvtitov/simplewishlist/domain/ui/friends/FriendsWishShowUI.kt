package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.LoginModel
import com.github.pvtitov.simplewishlist.domain.model.WishModel

interface FriendsWishShowUI {
    fun showFriendsWishDetails(login: LoginModel, wish: WishModel)
}