package com.github.pvtitov.noserver.protocol.impl

import com.github.pvtitov.noserver.protocol.ShareProtocol
import com.github.pvtitov.noserver.protocol.api.SharingManager
import com.github.pvtitov.noserver.protocol.api.SigningManager

internal class ShareProtocolImpl<T>(
    private val sharingManager: SharingManager<T>,
    private val signingManager: SigningManager<T>,
): ShareProtocol<T> {



    override fun download(): Result<T> {
        TODO("Not yet implemented")
    }

    override fun upload(data: T): Result<Boolean> {
        TODO("Not yet implemented")
    }
}