package com.github.pvtitov.simplewishlist.domain.ui.promises

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MakePromiseUI {
    fun makePromise(friend: User, wish: Wish)
}