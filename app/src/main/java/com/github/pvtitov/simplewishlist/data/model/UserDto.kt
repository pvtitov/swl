package com.github.pvtitov.simplewishlist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String? = null,
    val shareToken: String
)