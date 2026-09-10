package com.github.pvtitov.noserver.protocol.api

import com.github.pvtitov.noserver.protocol.model.SignedData

internal interface SharingManager<T> {

    fun send(signedData: SignedData<T>): Result<Boolean>

    fun download(signedData: SignedData<T>): Boolean
}