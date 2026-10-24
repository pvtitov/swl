package com.github.pvtitov.simplewishlist.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDto(
    val name: String? = null,
    val shareToken: String
) : Parcelable