package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MyWishEditUI {
    fun editWish(oldWish: Wish, newWish: Wish)
}