package com.github.pvtitov.simplewishlist.core.ui.promises

import com.github.pvtitov.simplewishlist.core.domain.Friend
import com.github.pvtitov.simplewishlist.core.domain.Me
import com.github.pvtitov.simplewishlist.core.domain.Wish

interface CancelPromiseUI {
    fun Me.cancelPromise(friend: Friend, wish: Wish)
}