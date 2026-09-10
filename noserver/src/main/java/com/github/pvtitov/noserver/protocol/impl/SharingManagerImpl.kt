package com.github.pvtitov.noserver.protocol.impl

import com.github.pvtitov.noserver.protocol.api.SharingManager
import com.github.pvtitov.noserver.protocol.model.SignedData

internal class SharingManagerImpl<T>: SharingManager<T> {
    override fun send(signedData: SignedData<T>): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun download(signedData: SignedData<T>): Boolean {
        TODO("Not yet implemented")
    }
}