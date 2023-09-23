package com.github.pvtitov.simplewishlist.domain.ui.mywishlist

import com.github.pvtitov.simplewishlist.domain.model.WishModel

interface MyWishDeleteUI {
    fun deleteWish(wish: WishModel)
}