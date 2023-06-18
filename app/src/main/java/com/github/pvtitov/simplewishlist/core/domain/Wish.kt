package com.github.pvtitov.simplewishlist.core.domain

data class Wish(
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val wishUrl: String? = null
)
