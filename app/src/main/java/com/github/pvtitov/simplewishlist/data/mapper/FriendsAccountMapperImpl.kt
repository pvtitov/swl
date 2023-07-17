package com.github.pvtitov.simplewishlist.data.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.mapper.FriendsAccountMapper
import com.github.pvtitov.simplewishlist.domain.model.Friend

interface FriendsAccountMapperImpl: FriendsAccountMapper {
    override fun map(
        login: String,
        friendsAccount: AccountData,
        friends: List<AccountData>
    ): Friend {
        return Friend(

        )
    }
}