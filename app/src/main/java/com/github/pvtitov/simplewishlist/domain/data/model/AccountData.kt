package com.github.pvtitov.simplewishlist.domain.data.model

import com.github.pvtitov.simplewishlist.domain.model.WishModel

/**
 * Модель data-слоя - персональный вишлист, одна модель и для своего вишлиста, и для вишлистов
 * друзей.
 */
data class AccountData(
    val login: String,
    val name: String? = null,
    val wishes: List<WishModel> = emptyList(),
    val promises: Map<String, List<WishModel>> = emptyMap(),
    val friends: List<String> = emptyList()
)