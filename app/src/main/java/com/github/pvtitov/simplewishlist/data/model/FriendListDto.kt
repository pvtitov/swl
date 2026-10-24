package com.github.pvtitov.simplewishlist.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendListDto(
    val friendList: List<UserDto>,
) : Parcelable