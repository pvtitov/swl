package com.github.pvtitov.simplewishlist.domain.model

data class WishModel(
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val wishUrl: String? = null
)
