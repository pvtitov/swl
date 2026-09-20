package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun WishesScreen(frindIndex: Int = PREVIEW_INDEX) {
    Text("WishesScreen(frindIndex = $frindIndex)")
}

private const val PREVIEW_INDEX = -1