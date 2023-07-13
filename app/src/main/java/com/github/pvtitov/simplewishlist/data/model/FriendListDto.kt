package com.github.pvtitov.simplewishlist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendListDto(
    val friendList: List<UserDto>,
)