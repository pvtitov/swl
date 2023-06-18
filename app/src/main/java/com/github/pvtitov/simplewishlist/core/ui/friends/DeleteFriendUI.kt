package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.Friend
import com.github.pvtitov.simplewishlist.core.domain.Me

interface DeleteFriendUI {
    fun Me.forgetFriend(friend: Friend)
}