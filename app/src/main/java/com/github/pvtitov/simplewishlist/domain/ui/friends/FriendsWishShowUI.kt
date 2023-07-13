package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface FriendsWishShowUI {
    fun Friend.showWish(wish: Wish)
}