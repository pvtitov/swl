package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.composable.element.ImageComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun WishComposable(
    wish: Wish = PREVIEW_WISH,
    viewModel: MainViewModel = MainViewModel(),
) {
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingL = dimensionResource(id = R.dimen.padding_l)

    Card {
        Column(
            modifier = Modifier
                .padding(paddingL)
                .fillMaxSize(),
        ) {
            ImageComposable(
                modifier = Modifier
                    .padding(bottom = paddingL)
                    .aspectRatio(1f)
                    .fillMaxWidth(),
                imageUrl = wish.wishUrl,
                loadingPlaceholderId = R.drawable.ic_placeholder_24,
                failurePlaceholderId = R.drawable.ic_placeholder_24,
            )
            Text(
                modifier = Modifier
                    .padding(bottom = paddingS),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = wish.title
            )
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = paddingS)
            ) {
                Button(
                    modifier = Modifier
                        .padding(end = paddingS),
                    onClick = {
                        viewModel.onClickEditWish(wish)
                    }
                ) {
                    Text(text = stringResource(id = R.string.wish_button_edit))
                }
                Button(
                    onClick = {
                        viewModel.onClickDeleteWish(wish)
                    }
                ) {
                    Text(text = stringResource(id = R.string.wish_button_delete))
                }
            }
            Text(
                modifier = Modifier
                    .padding(bottom = paddingS),
                text = wish.description ?: ""
            )
            wish.wishUrl?.let { url ->
                ClickableText(
                    text = AnnotatedString(url)
                ) {
                    // TODO open URL
                }
            }
        }
    }
}

private val PREVIEW_WISH = Wish(
    title = "Title",
    description = "Description",
    imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg",
    wishUrl = "https://google.com"
)