package com.github.pvtitov.simplewishlist.domain.ui.promises

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface CancelPromiseUI {
    fun cancelPromise(friend: User, wish: Wish)
}