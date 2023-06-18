package com.github.pvtitov.simplewishlist.core.domain

import java.net.URL

data class Wish(
    val title: String,
    val description: String? = null,
    val imageUrl: URL? = null,
    val wishUrl: URL? = null
)
