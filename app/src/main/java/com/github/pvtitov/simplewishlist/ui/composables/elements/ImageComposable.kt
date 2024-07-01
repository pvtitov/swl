package com.github.pvtitov.simplewishlist.ui.composables.elements

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageComposable(
    imageUrl: String? = null,
    @DrawableRes loadingPlaceholderId: Int? = null,
    @DrawableRes failurePlaceholderId: Int? = null,
    modifier: Modifier = Modifier
) {
    imageUrl?.let { url ->
        GlideImage(
            model = url,
            modifier = modifier.padding(0.dp),
            contentDescription = null,
            loading = loadingPlaceholderId?.let { id -> placeholder(id) },
            failure = failurePlaceholderId?.let { id -> placeholder(id) }
        )
    }
}