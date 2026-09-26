package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.composable.item.WishItem
import com.github.pvtitov.simplewishlist.ui.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
fun WishesScreen(
    friendIndex: Int = PREVIEW_INDEX,
    navigation: Navigation = PREVIEW_NAVIGATION
) {
    val wishesState = stubWishes().collectAsStateWithLifecycle()

    val paddingM = dimensionResource(R.dimen.padding_m)
    val avatarSizeM = dimensionResource(R.dimen.avatar_m)

    LazyColumn(
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        item {
            Row {
                AsyncImage(
                    model = "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80",
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(avatarSizeM)
                        .clip(CircleShape),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
                    error = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop,
                )

                Column(
                    modifier = Modifier.padding(start = paddingM)
                ) {
                    Text(
                        text = "My friend's name",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = "mail@example.com",
                    )
                }
            }
        }
        wishesState.value.forEach { wish ->
            item {
                WishItem(
                    wish, url = when (wish.wishIndex) {
                        0 -> null
                        1 -> "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80"
                        2 -> ""
                        3 -> "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head.jpg"
                        else -> if (wish.wishIndex % 2 == 0) "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80" else null
                    }
                )
            }
        }
    }
}

private fun stubWishes(): StateFlow<List<Screen.Wish>> {
    return MutableStateFlow(
        List(20) { i ->
            Screen.Wish(0, i)
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
private const val PREVIEW_INDEX = -1