package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.element.imagePreviewPlaceholder

@Preview
@Composable
fun WishScreen(
    friendIndex: Int = PREVIEW_INDEX,
    wishIndex: Int = PREVIEW_INDEX
) {
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val imagePreviewHeight = dimensionResource(id = R.dimen.wish_image_preview_height)
    val imagePreviewCornerRadius = dimensionResource(id = R.dimen.corner_radius_m)
    val imagePreviewShape = remember { RoundedCornerShape(imagePreviewCornerRadius) }
    val imageUrl: String? = remember { null }
    val title: String? = remember { null }
    val description: String? = remember { null }
    val wishUrl: String? = remember { null }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        val modifier = Modifier.fillMaxWidth()
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .height(imagePreviewHeight)
                .clip(imagePreviewShape),
            placeholder = imagePreviewPlaceholder(),
            error = imagePreviewPlaceholder(),
            contentScale = ContentScale.Crop,
        )

        if (title != null) {
            Text(
                text = title,
                modifier = modifier,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        if (description != null) {
            Text(
                text = description,
                modifier = modifier
            )
        }

        if (wishUrl != null) {
            Text(
                text = buildAnnotatedString {
                    withLink(
                        link = LinkAnnotation.Url(
                            url = wishUrl,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                )
                            )
                        )
                    ) {
                        append(wishUrl)
                    }
                },
                modifier = modifier,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private const val PREVIEW_INDEX = -1