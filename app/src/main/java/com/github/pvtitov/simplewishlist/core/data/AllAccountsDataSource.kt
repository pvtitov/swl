package com.github.pvtitov.simplewishlist.core.data

import com.github.pvtitov.simplewishlist.core.domain.Friend

interface AllAccountsDataSource {
    /**
     * @return null если аккаунт не найден или найдено больше одного аккаунта по запросу [login].
     * Предполагается, что нужно знать точный логин своего друга.
     */
    suspend fun findAccount(login: String): Friend?
}