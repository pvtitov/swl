package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.theme.PaddingM
import com.github.pvtitov.simplewishlist.ui.theme.PaddingS
import com.github.pvtitov.simplewishlist.ui.theme.PaddingXS
import com.github.pvtitov.simplewishlist.ui.theme.WishItemHeight
import com.github.pvtitov.simplewishlist.ui.theme.WishItemImageSize

@Preview
@Composable
fun WishComposable(wish: Wish = PREVEIW_WISH) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(WishItemHeight)
    ) {
        Row(
            modifier = Modifier.padding(PaddingM)
        ) {
            ImageComposable(
                imageUrl = wish.wishUrl,
                loadingPlaceholderId = com.github.pvtitov.simplewishlist.R.drawable.image_placeholder_24,
                modifier = Modifier
                    .padding(
                        start = PaddingXS,
                        top = PaddingXS,
                        bottom = PaddingXS
                    )
                    .height(WishItemImageSize)
                    .width(WishItemImageSize)

            )
            Column {
                Text(
                    modifier = Modifier
                        .padding(start = PaddingS, top = PaddingS, end = PaddingS),
                    text = wish.title)
                Text(
                    modifier = Modifier
                        .padding(start = PaddingS, top = PaddingS, end = PaddingS),
                    text = wish.description ?: "")
                wish.wishUrl?.let { url ->
                    ClickableText(
                        modifier = Modifier
                            .padding(start = PaddingS, top = PaddingS, end = PaddingS),
                        text = AnnotatedString(url)) {

                    }
                }
            }
        }
    }
}

private val PREVEIW_WISH = Wish(
    title = "Preview title",
    description = "Preview description",
    imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg",
    wishUrl = "https://google.com"
)