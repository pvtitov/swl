package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.model.Wish

@Composable
fun NewWishScreen(
    onTopBarTitle: (String) -> Unit,
    isSaveClicked: Boolean,
    onSaveClickHandled: (Wish) -> Unit
) {
    val topBarTitle = stringResource(R.string.new_wish_title)
    onTopBarTitle(topBarTitle)
    MyWishScreen(
        null,
        {},
        isSaveClicked,
        onSaveClickHandled = { onSaveClickHandled(it) }
    )
}

@Preview
@Composable
fun NewWishScreenPreview() {
    NewWishScreen({}, false, {})
}