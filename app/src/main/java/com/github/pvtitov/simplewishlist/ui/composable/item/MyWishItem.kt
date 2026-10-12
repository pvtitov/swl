package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun MyWishItem(
    wish: Wish,
    onClick: () -> Unit,
) {
    val imageHeight = dimensionResource(R.dimen.wish_item_image_height)
    val paddingM = dimensionResource(R.dimen.padding_m)

    val link = stringResource(R.string.link)
    val more = stringResource(R.string.more)
    val delete = stringResource(R.string.delete)

    val expandedState = remember { mutableStateOf(false) }

    val uriHandler = LocalUriHandler.current

    val (title, description, url, imageUrl) = wish
    var isImageError by remember { mutableStateOf(imageUrl.isNullOrBlank()) }

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
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

            Column(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = {
                        expandedState.value = true
                    },
                    modifier = Modifier.padding(paddingM),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_20),
                        contentDescription = more,
                    )
                }

                DropdownMenu(
                    expanded = expandedState.value,
                    onDismissRequest = { expandedState.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(delete) },
                        onClick = { expandedState.value = false }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyWishItemPreview() {
    MyWishItem(
        Wish("Title", "Description", null, null),
        {}
    )
}