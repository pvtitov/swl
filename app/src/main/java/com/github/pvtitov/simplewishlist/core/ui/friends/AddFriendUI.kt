package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.Friend
import com.github.pvtitov.simplewishlist.core.domain.Me

interface AddFriendUI {
    fun Me.saveFriend(friend: Friend)
}