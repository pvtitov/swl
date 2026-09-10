package com.github.pvtitov.noserver.protocol

import android.os.Parcelable
import com.github.pvtitov.noserver.protocol.interactor.Interactor
import com.github.pvtitov.noserver.protocol.model.Address
import com.github.pvtitov.noserver.protocol.model.SignedData
import com.github.pvtitov.noserver.protocol.model.User

class NoServer(private val dataType: Class<*>) {

    private val interactor by lazy { Interactor(dataType) }

    fun download(address: Address): T {
        interactor
    }
}