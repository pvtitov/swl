package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.composable.item.WishItemComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun WishListComposable(
    wishlist: List<Wish> = PREVIEW_WISH_LIST,
    viewModel: MainViewModel = MainViewModel()
) {

    LazyColumn {
        wishlist.forEachIndexed { index, wish ->
            item {
                WishItemComposable(wish, viewModel, index == 0)
            }
        }
    }
}

private val PREVIEW_WISH_LIST = List(size = 20) { i ->
    Wish(
        title = "Title $i",
        description = "Description $i",
        imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg",
        wishUrl = "https://google.com"
    )
}