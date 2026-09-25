package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.item.MyWishItem
import com.github.pvtitov.simplewishlist.ui.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
fun MyWishesScreen() {

    val wishesState = stubWishes().collectAsStateWithLifecycle()

    val paddingM = dimensionResource(R.dimen.padding_m)

    LazyColumn(
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        wishesState.value.forEach { myWish ->
            item {
                MyWishItem(
                    myWish, url = when (myWish.wishIndex) {
                        0 -> null
                        1 -> "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80"
                        2 -> ""
                        3 -> "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head.jpg"
                        else -> if (myWish.wishIndex % 2 == 0) "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80" else null
                    }
                )
            }
        }
    }
}

private fun stubWishes(): StateFlow<List<Screen.MyWish>> {
    return MutableStateFlow(
        List(20) { i ->
            Screen.MyWish(i)
        }
    ).asStateFlow()
}