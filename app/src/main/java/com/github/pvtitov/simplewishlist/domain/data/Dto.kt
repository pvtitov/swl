package com.github.pvtitov.simplewishlist.domain.data

import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import kotlinx.serialization.Serializable

@Serializable
data class Dto(
    val data: List<UserData>,
    val sender: User
)
