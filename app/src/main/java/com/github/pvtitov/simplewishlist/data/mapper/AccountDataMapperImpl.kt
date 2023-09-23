package com.github.pvtitov.simplewishlist.data.mapper

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.mapper.AccountDataMapper
import com.github.pvtitov.simplewishlist.domain.model.LoginModel
import com.github.pvtitov.simplewishlist.domain.model.UserDataModel

interface AccountDataMapperImpl : AccountDataMapper {
    override fun map(accountData: AccountData): UserDataModel {
        return with(accountData) {
            UserDataModel(
                login = LoginModel(login),
                name = name ?: login,
                wishes = wishes,
                promises = promises,
                friends = friends
            )
        }
    }
}