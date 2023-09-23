package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.WishModel

interface MyWishEditUI {
    fun editWish(oldWish: WishModel, newWish: WishModel)
}