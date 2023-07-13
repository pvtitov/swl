package com.github.pvtitov.simplewishlist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WishDto(
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val url: String? = null
)
