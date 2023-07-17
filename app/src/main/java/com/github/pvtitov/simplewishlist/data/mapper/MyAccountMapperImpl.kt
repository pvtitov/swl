package com.github.pvtitov.simplewishlist.data.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.mapper.FriendsAccountMapper
import com.github.pvtitov.simplewishlist.domain.mapper.MyAccountMapper
import com.github.pvtitov.simplewishlist.domain.model.Friend
import com.github.pvtitov.simplewishlist.domain.model.Me

interface MyAccountMapperImpl: MyAccountMapper {
    override fun map(
        login: String,
        myAccount: AccountData,
        friends: List<AccountData>
    ): Me {
        return Me(
            login = login,
            name = 
        )
    }
}