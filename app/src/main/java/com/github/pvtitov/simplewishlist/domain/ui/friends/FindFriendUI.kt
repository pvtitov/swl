package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.User

interface FindFriendUI {
    fun findUser(query: String): List<User>
}