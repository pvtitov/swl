package com.github.pvtitov.simplewishlist.core.ui.mywishlist

import com.github.pvtitov.simplewishlist.core.domain.Me
import com.github.pvtitov.simplewishlist.core.domain.Wish

interface MyWishEditUI {
    fun Me.editWish(oldWish: Wish, newWish: Wish)
}