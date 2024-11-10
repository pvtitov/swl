package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun NewWishComposable(
    viewModel: MainViewModel = MainViewModel(),
) {
    EditWishComposable(null, viewModel)
}