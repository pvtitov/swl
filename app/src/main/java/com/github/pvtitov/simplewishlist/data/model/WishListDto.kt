package com.github.pvtitov.simplewishlist.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishListDto(
    val wishList: List<WishDto>,
) : Parcelable