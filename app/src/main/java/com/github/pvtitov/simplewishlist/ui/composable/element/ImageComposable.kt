package com.github.pvtitov.simplewishlist.ui.composable.element

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageComposable(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    @DrawableRes loadingPlaceholderId: Int,
    @DrawableRes failurePlaceholderId: Int,
) {
    GlideImage(
        model = imageUrl,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        loading = placeholder(loadingPlaceholderId),
        failure = placeholder(failurePlaceholderId),
    )
}