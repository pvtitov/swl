package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MyWishCreateUI {
    fun createWish(wish: Wish)
}