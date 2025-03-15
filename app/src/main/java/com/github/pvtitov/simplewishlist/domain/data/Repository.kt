package com.github.pvtitov.simplewishlist.domain.data

interface Repository<T> {

    suspend fun download(): T?

    suspend fun upload(data: T): Boolean
}