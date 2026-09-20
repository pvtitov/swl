package com.github.pvtitov.simplewishlist.ui.model

sealed interface Screen {
    data object Login : Screen
    data object MyWishes : Screen
    data object NewWish : Screen
    data class MyWish(val wishIndex: Int) : Screen
    data object Friends : Screen
    data class Wishes(val friendIndex: Int) : Screen
    data class Wish(val friendIndex: Int, val wishIndex: Int) : Screen
}