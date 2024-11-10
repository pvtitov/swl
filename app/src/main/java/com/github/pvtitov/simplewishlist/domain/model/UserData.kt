package com.github.pvtitov.simplewishlist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val user: User,
    val wishList: List<Wish>,
    val promises: Map<String, List<Wish>>,
    val date: Long,
    val checkSum: Int
)