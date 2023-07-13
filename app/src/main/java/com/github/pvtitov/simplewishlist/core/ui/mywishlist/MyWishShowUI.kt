package com.github.pvtitov.simplewishlist.core.ui.mywishlist

import com.github.pvtitov.simplewishlist.core.domain.model.Me
import com.github.pvtitov.simplewishlist.core.domain.model.Wish

interface MyWishShowUI {
    fun Me.showWish(wish: Wish)
}