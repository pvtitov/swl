package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.composable.element.ImageComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun DeleteWishComposable(
    wish: Wish = PREVIEW_WISH,
    viewModel: MainViewModel = MainViewModel(),
    coroutineScope: CoroutineScope
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
                    .fillMaxWidth()
                    .clip(CircleShape),
                imageUrl = wish.imageUrl,
                loadingPlaceholderId = R.drawable.ic_placeholder_24,
                failurePlaceholderId = R.drawable.ic_placeholder_24,
            )
            Text(
                modifier = Modifier
                    .padding(bottom = paddingS),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = wish.title,
                style = MaterialTheme.typography.displayLarge
            )
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = paddingL)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickConfirmDeleteWish(wish)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.wish_button_confirm_delete),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(bottom = paddingS),
                text = wish.description ?: "",
                style = MaterialTheme.typography.bodyLarge
            )
            wish.wishUrl?.let { url ->
                Text(
                    text = buildAnnotatedString {
                        pushStyle(
                            SpanStyle(color = Color.Blue)
                        )
                        pushLink(
                            LinkAnnotation.Url(url)
                        )
                        append(url)
                    },
                    style = MaterialTheme.typography.labelLarge
                )
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