package com.github.pvtitov.simplewishlist.domain.model

/**
 * Вся информация о текущем пользователе.
 *
 * Умышленно сделаны разные сущности для себя и друзей, чтобы подчеркнуть логическую разницу
 * этих двух ролей. Например, и у себя, и у друзей есть свойство [wishList], но допустимые действия
 * с ним разные - только у себя можно редактировать.
 */
data class Me(
    val login: String,
    val name: String = login,
    val wishList: List<Wish>,
    val promises: Map<Friend, List<Wish>>,
    val friendList: List<Friend>
)