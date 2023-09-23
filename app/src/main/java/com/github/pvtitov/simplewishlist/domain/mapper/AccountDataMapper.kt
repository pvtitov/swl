package com.github.pvtitov.simplewishlist.domain.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.model.UserDataModel

interface AccountDataMapper {
    fun map(accountData: AccountData): UserDataModel
}