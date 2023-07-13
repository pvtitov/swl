package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.composable.item.WishItemComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun WishListComposable(
    wishlist: List<Wish> = PREVIEW_WISH_LIST,
    viewModel: MainViewModel = MainViewModel(),
    coroutineScope: CoroutineScope
) {

    LazyColumn {
        wishlist.forEachIndexed { index, wish ->
            item {
                WishItemComposable(wish, viewModel, index == 0, coroutineScope)
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