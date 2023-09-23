package com.github.pvtitov.simplewishlist.domain.model

data class UserDataModel(
    val login: LoginModel,
    val name: String,
    val wishes: List<WishModel>,
    val promises: Map<String, List<WishModel>>,
    val friends: List<String>
)