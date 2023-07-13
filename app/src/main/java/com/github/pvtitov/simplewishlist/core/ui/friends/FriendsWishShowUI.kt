package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.model.Friend
import com.github.pvtitov.simplewishlist.core.domain.model.Wish

interface FriendsWishShowUI {
    fun Friend.showWish(wish: Wish)
}