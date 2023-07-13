package com.github.pvtitov.simplewishlist.domain.data.model

import com.github.pvtitov.simplewishlist.domain.model.Wish

/**
 * Модель data-слоя - персональный вишлист, одна модель и для своего вишлиста, и для вишлистов
 * друзей.
 */
data class AccountData(
    val name: String? = null,
    val wishes: List<Wish> = emptyList(),
    val promises: Map<String, List<Wish>> = emptyMap(),
    val friends: List<String> = emptyList()
)