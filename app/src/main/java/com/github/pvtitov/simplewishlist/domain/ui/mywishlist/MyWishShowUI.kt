package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MyWishShowUI {
    fun showMyWish(wish: Wish)
}