package com.github.pvtitov.noserver.protocol.model

data class SignedData<T>(
    val data: T,
    val signature: Signature
)

data class Signature(
    val time: Long
)