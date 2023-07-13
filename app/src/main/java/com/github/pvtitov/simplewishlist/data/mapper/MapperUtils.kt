package com.github.pvtitov.simplewishlist.data.mapper

import com.github.pvtitov.simplewishlist.data.model.UserDto
import com.github.pvtitov.simplewishlist.data.model.WishDto
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.Wish

internal fun UserDto.toDomain(): User = User(
    id = getId(),
    name = name
)

internal fun User.toData(): UserDto = UserDto(
    name = name,
    shareToken = getShareToken()
)

internal fun WishDto.toDomain(): Wish = Wish(
    title = title,
    description = description,
    imageUrl = imageUrl,
    url = url
)

internal fun Wish.toData(): WishDto = WishDto(
    title = title,
    description = description,
    imageUrl = imageUrl,
    url = url
)

private fun UserDto.getId(): String {
    return shareToken
}

private fun User.getShareToken(): String {
    return id
}