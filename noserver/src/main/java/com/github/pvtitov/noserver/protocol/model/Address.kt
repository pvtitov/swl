package com.github.pvtitov.noserver.protocol.model

sealed interface Address

data class GoogleAddress(val url: String) : Address

data class ManualAddress(val url: String) : Address