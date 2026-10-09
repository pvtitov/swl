package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R

@Preview
@Composable
fun NewWishScreen(
    onTopBarTitle: (String) -> Unit = {}
) {

    val topBarTitle = stringResource(R.string.new_wish_title)
    onTopBarTitle(topBarTitle) // TODO replace test implementation
    MyWishScreen()
}