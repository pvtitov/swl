package com.github.pvtitov.simplewishlist.core.ui.mywishlist

import com.github.pvtitov.simplewishlist.core.domain.Me
import com.github.pvtitov.simplewishlist.core.domain.Wish

interface MyWishShowUI {
    fun Me.showWish(wish: Wish)
}