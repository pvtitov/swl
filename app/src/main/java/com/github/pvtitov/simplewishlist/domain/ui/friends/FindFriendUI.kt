package com.github.pvtitov.simplewishlist.domain.ui.friends

import com.github.pvtitov.simplewishlist.domain.model.Friend

interface FindFriendUI {
    fun findFriend(query: String): List<Friend>
}