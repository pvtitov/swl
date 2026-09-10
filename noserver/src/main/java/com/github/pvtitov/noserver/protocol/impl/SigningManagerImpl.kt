package com.github.pvtitov.noserver.protocol.impl

import com.github.pvtitov.noserver.protocol.api.SigningManager
import com.github.pvtitov.noserver.protocol.model.SignedData

internal class SigningManagerImpl<T>: SigningManager<T> {
    override fun send(signedData: SignedData<T>): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun download(signedData: SignedData<T>): Boolean {
        TODO("Not yet implemented")
    }
}