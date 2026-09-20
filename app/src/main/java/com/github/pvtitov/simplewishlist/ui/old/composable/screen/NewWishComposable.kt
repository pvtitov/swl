package com.github.pvtitov.simplewishlist.ui.old.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.ui.old.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun NewWishComposable(
    viewModel: MainViewModel = MainViewModel(),
    coroutineScope: CoroutineScope
) {
    EditWishComposable(null, viewModel, coroutineScope)
}