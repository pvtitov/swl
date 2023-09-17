package com.github.pvtitov.simplewishlist.domain.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.model.User

interface MyAccountMapper {
    fun map(
        login: String,
        myAccount: AccountData,
        friends: List<AccountData>
    ): User
}