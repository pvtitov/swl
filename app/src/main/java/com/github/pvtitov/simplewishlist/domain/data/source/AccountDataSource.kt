package com.github.pvtitov.simplewishlist.domain.data.source

import com.github.pvtitov.simplewishlist.domain.model.LoginModel
import com.github.pvtitov.simplewishlist.domain.model.UserDataModel

interface AccountDataSource {
    suspend fun downloadUserData(login: LoginModel): UserDataModel?

    suspend fun uploadUserData(userData: UserDataModel): Boolean
}