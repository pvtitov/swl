package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.model.Screen

@Preview
@Composable
fun WishItem(
    wish: Screen.Wish = PREVIEW_WISH,
    navigation: Navigation = PREVIEW_NAVIGATION,
    url: String? = null
) {
    val imageHeight = dimensionResource(R.dimen.wish_item_image_height)
    val paddingM = dimensionResource(R.dimen.padding_m)

    val link = stringResource(R.string.link)
    val more = stringResource(R.string.more)

    val isImageVisibleState = remember { mutableStateOf(!url.isNullOrEmpty()) }

    Card(
        modifier = Modifier.wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isImageVisibleState.value) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.height(imageHeight),
                    contentScale = ContentScale.Crop,
                    onError = {
                        isImageVisibleState.value = false
                    }
                )
            }

            Text(
                text = "Title ${wish.wishIndex}",
                modifier = Modifier.padding(paddingM),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = "Description ${wish.wishIndex}",
                modifier = Modifier.padding(PaddingValues(start = paddingM, end = paddingM, bottom = paddingM))
            )

            AssistChip(
                onClick = {
                    // TODO
                },
                label = {
                    Text(
                        text = link,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = Modifier.padding(PaddingValues(start = paddingM, end = paddingM, bottom = paddingM)),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_link_12),
                        contentDescription = null
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
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

private val PREVIEW_WISH = Screen.Wish(0, 0)