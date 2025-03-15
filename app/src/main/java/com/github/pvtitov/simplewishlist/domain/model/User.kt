package com.github.pvtitov.simplewishlist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val login: String,
    val name: String? = null,
    val imageUrl: String? = null,
    val wishList: WishList? = null
)