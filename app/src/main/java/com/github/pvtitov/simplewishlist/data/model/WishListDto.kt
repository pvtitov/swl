package com.github.pvtitov.simplewishlist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WishListDto(
    val wishList: List<WishDto>,
)