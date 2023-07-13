package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.model.Wish

@Composable
fun WishItem(
    wish: Wish,
    onClick: () -> Unit
) {
    val imageHeight = dimensionResource(R.dimen.wish_item_image_height)
    val paddingM = dimensionResource(R.dimen.padding_m)

    val link = stringResource(R.string.link)

    val (title, description, url, imageUrl) = wish
    var isImageError by remember { mutableStateOf(imageUrl.isNullOrBlank()) }

    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (!isImageError) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.height(imageHeight),
                    contentScale = ContentScale.Crop,
                    onError = {
                        isImageError = true
                    }
                )
            }

            title?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(paddingM),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            description?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = paddingM,
                            end = paddingM,
                            bottom = paddingM
                        )
                    )
                )
            }

            if (!url.isNullOrBlank()) {
                AssistChip(
                    onClick = {
                        uriHandler.openUri(url)
                    },
                    label = {
                        Text(
                            text = link,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = paddingM,
                            end = paddingM,
                            bottom = paddingM
                        )
                    ),
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
}

@Preview
@Composable
fun WishItemPreview() {
    WishItem(
        Wish("Title", "Description", null, null),
        {}
    )
}