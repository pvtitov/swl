package com.github.pvtitov.simplewishlist.domain.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Me

interface FriendsAccountMapper {
    fun map(
        login: String,
        friendsAccount: AccountData,
        friends: List<AccountData>
    ): Friend
}