package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.Friend
import com.github.pvtitov.simplewishlist.core.domain.Wish

interface FriendsWishShowUI {
    fun Friend.showWish(wish: Wish)
}