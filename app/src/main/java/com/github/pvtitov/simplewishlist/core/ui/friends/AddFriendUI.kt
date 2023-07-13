package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.model.Friend
import com.github.pvtitov.simplewishlist.core.domain.model.Me

interface AddFriendUI {
    fun Me.saveFriend(friend: Friend)
}