package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.UserDataModel

interface FindFriendUI {
    fun findUser(query: String): List<UserDataModel>
}