package com.github.pvtitov.simplewishlist.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class Wish(
    val title: String?,
    val description: String?,
    val link: String?,
    val imageLink: String?
)