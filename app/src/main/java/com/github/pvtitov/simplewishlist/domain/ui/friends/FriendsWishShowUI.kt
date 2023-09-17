package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface FriendsWishShowUI {
    fun User.showWishDetails(wish: Wish)
}