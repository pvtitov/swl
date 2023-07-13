package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.Me
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MyWishShowUI {
    fun Me.showWish(wish: Wish)
}