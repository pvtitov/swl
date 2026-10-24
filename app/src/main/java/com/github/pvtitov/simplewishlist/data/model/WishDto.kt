package com.github.pvtitov.simplewishlist.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishDto(
    val title: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val url: String? = null
) : Parcelable
