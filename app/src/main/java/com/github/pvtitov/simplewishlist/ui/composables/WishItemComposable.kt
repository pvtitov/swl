package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish

@Preview
@Composable
fun WishItemComposable(wish: Wish = PREVEIW_WISH) {
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val imageSize = dimensionResource(id = R.dimen.wish_item_image_size)

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(paddingM)
        ) {
            ImageComposable(
                imageUrl = wish.wishUrl,
                loadingPlaceholderId = R.drawable.ic_placeholder_24,
                failurePlaceholderId = R.drawable.ic_placeholder_24,
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = paddingM)
            ) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = wish.title,
                )
                Text(
                    modifier = Modifier
                        .padding(top = paddingS),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = wish.description ?: ""
                )
                wish.wishUrl?.let { url ->
                    ClickableText(
                        modifier = Modifier
                            .padding(top = paddingS),
                        text = AnnotatedString(url)
                    ) {

                    }
                }
            }
        }
    }
}

private val PREVEIW_WISH = Wish(
    title = "Preview title",
    description = "Preview description very long text very long text very long text very long text" +
            " very long text very long text very long text very long text very long text",
    imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg",
    wishUrl = "https://google.com"
)