package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.item.MyWishItem
import com.github.pvtitov.simplewishlist.ui.model.Wish

@Composable
fun MyWishesScreen(
    wishes: List<Wish>,
    onMyWishClicked: (Int) -> Unit,
    onTopBarTitle: (String) -> Unit
) {
    val paddingM = dimensionResource(R.dimen.padding_m)

    val topBarTitle = stringResource(R.string.my_wishes_title)
    onTopBarTitle(topBarTitle)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        wishes.forEachIndexed { index, wish ->
            item {
                MyWishItem(
                    wish = wish,
                    onClick = { onMyWishClicked(index) },
                )
            }
        }
    }
}

@Preview
@Composable
fun MyWishesScreenPreview() {
    MyWishesScreen(
        listOf(Wish("title", "description", null, null)),
        {},
        {}
    )
}