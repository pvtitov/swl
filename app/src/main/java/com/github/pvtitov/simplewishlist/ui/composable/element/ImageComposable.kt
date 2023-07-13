package com.github.pvtitov.simplewishlist.ui.composable.element

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory

@Composable
fun ImageComposable(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    @DrawableRes loadingPlaceholderId: Int,
    @DrawableRes failurePlaceholderId: Int,
) {
    if (imageUrl != null) {
        val imageLoader = ImageLoader.Builder(LocalContext.current).components {
                add(OkHttpNetworkFetcherFactory())
            }.build()

        AsyncImage(
            model = imageUrl,
            imageLoader = imageLoader,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(id = failurePlaceholderId),
            fallback = painterResource(id = failurePlaceholderId),
            placeholder = painterResource(id = loadingPlaceholderId),
            onLoading = {
                Log.d(TAG, imageUrl)
            },
            onError = { state ->
                Log.e(TAG, state.result.throwable.message, state.result.throwable)
            }
        )
    } else {
        Image(
            painter = painterResource(id = failurePlaceholderId),
            contentDescription = null,
            modifier = modifier
        )
    }
}

private val TAG = "ImageComposable"