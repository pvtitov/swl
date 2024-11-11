package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.composable.element.ImageComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun WishItemComposable(
    wish: Wish = PREVEIW_WISH,
    viewModel: MainViewModel = MainViewModel()
) {
    val paddingXS = dimensionResource(id = R.dimen.padding_xs)
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val imageSize = dimensionResource(id = R.dimen.wish_item_image_size)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingXS)
            .clickable {
                viewModel.onClickWish(wish)
            }
    ) {
        Row(
            modifier = Modifier.padding(paddingM)
        ) {
            ImageComposable(
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize),
                imageUrl = wish.wishUrl,
                loadingPlaceholderId = R.drawable.ic_placeholder_24,
                failurePlaceholderId = R.drawable.ic_placeholder_24,
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
                    Text(
                        modifier = Modifier
                            .padding(top = paddingS),
                        text = buildAnnotatedString {
                            pushStyle(
                                SpanStyle(color = Color.Blue)
                            )
                            pushLink(
                                LinkAnnotation.Url(url)
                            )
                            append(url)
                        }
                    )
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