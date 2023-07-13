package com.github.pvtitov.simplewishlist.core.ui.promises

import com.github.pvtitov.simplewishlist.core.domain.model.Friend
import com.github.pvtitov.simplewishlist.core.domain.model.Me
import com.github.pvtitov.simplewishlist.core.domain.model.Wish

interface MakePromiseUI {
    fun Me.makePromise(friend: Friend, wish: Wish)
}