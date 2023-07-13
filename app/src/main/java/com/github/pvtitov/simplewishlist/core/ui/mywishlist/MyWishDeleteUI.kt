package com.github.pvtitov.simplewishlist.core.ui.mywishlist

import com.github.pvtitov.simplewishlist.core.domain.model.Me
import com.github.pvtitov.simplewishlist.core.domain.model.Wish

interface MyWishDeleteUI {
    fun Me.deleteWish(wish: Wish)
}