package com.github.pvtitov.simplewishlist.domain.data

interface Repository {
    suspend fun import(): Dto?

    suspend fun export(data: Dto): Boolean
}