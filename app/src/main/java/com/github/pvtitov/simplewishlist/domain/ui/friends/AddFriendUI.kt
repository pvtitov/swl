package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.User

interface AddFriendUI {
    fun addToMyFriendList(friend: User)
}