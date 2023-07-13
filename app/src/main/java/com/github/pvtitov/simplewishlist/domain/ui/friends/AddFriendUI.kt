package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Me

interface AddFriendUI {
    fun Me.saveFriend(friend: Friend)
}