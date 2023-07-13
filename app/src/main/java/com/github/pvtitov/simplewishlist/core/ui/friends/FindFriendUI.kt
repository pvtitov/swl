package com.github.pvtitov.simplewishlist.core.ui.friends

import com.github.pvtitov.simplewishlist.core.domain.model.Friend

interface FindFriendUI {
    fun findFriend(query: String): List<Friend>
}