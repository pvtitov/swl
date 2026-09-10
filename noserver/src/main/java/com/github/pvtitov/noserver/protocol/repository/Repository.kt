package com.github.pvtitov.noserver.protocol.repository

import com.github.pvtitov.noserver.protocol.model.Address
import com.github.pvtitov.noserver.protocol.model.User

interface Repository<T> {
    fun download(address: Address): T
    fun upload(data: T)
}