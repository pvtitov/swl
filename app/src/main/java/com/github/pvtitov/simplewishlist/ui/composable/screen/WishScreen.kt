package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun WishScreen(
    friendIndex: Int = PREVIEW_INDEX,
    wishIndex: Int = PREVIEW_INDEX
) {
    Text("WishScreen(friendIndex = $friendIndex, wishIndex = $wishIndex)")
}

private const val PREVIEW_INDEX = -1