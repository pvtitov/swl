package com.github.pvtitov.simplewishlist.domain.model

data class UserData(
    val user: User,
    val wishList: List<Wish>,
    val promises: Map<User, List<Wish>>,
    val date: Long,
    val checkSum: Int
)