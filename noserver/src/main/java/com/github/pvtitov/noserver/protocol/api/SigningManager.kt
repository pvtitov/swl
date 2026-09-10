package com.github.pvtitov.noserver.protocol.api

import com.github.pvtitov.noserver.protocol.model.SignedData

internal interface SigningManager<T> {

    fun sign(): SignedData<T>

    fun verify(signedData: SignedData<T>): Boolean
}