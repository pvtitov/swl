package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.LoginModel

interface AddFriendUI {
    fun addToMyFriendList(friend: LoginModel)
}