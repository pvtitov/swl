package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.composable.item.MyWishItem
import com.github.pvtitov.simplewishlist.ui.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
fun MyWishesScreen(
    navigation: Navigation = PREVIEW_NAVIGATION
) {
    val wishesState = stubWishes().collectAsStateWithLifecycle()

    val paddingM = dimensionResource(R.dimen.padding_m)

    LazyColumn(
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        wishesState.value.forEach { myWish ->
            item {
                MyWishItem(
                    myWish = myWish,
                    navigation = navigation,
                    url = null // TODO
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

private val PREVIEW_NAVIGATION = object : Navigation {
    override val currentScreenState: State<Screen>
        get() = mutableStateOf(Screen.Login)

    override val isBackAvailable: Boolean
        get() = false

    override fun open(screen: Screen) {
    }

    override fun back() {
    }
}