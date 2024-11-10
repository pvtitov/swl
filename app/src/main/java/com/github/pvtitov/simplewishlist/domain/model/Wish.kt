package com.github.pvtitov.simplewishlist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Wish(
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val wishUrl: String? = null
)
