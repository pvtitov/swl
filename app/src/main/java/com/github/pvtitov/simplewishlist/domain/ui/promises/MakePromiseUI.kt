package com.github.pvtitov.simplewishlist.domain.ui.promises

import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Me
import com.github.pvtitov.simplewishlist.domain.model.Wish

interface MakePromiseUI {
    fun Me.makePromise(friend: Friend, wish: Wish)
}