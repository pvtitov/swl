package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.UserData

interface FindFriendUI {
    fun findUser(query: String): List<UserData>
}