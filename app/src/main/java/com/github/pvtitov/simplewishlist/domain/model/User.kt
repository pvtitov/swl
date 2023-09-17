package com.github.pvtitov.simplewishlist.domain.model

data class User(
    val login: String,
    val name: String = login,
    val wishList: List<Wish>,
    val promises: Map<User, List<Wish>>,
    val friendList: List<User>
)