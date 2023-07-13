package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Me

interface DeleteFriendUI {
    fun Me.forgetFriend(friend: Friend)
}