package com.github.pvtitov.simplewishlist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WishList(
    val friends: List<User>,
    val wishes: List<Wish>,
    val promises: Map<String, Wish>
)