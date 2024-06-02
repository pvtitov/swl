package com.github.pvtitov.simplewishlist.domain.model

data class User(
    val login: String,
    val name: String,
    val imageUrl: String? = null
)