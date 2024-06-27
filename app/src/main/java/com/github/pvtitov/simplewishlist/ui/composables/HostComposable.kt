package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreenModel
import com.github.pvtitov.simplewishlist.ui.theme.PaddingM
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme

@Preview
@Composable
fun HostComposable(
    screenModel: ScreenModel = PREVIEW_SCREEN_MODEL,
    contentComposable: @Composable (ScreenModel) -> Unit = PREVIEW_CONTENT_COMPOSABLE
) {
    SimpleWishListTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                contentComposable(screenModel)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = PaddingM, bottom = PaddingM),
                        onClick = {
                            // TODO
                        },
                    ) {
                        Text(
                            modifier = Modifier.padding(PaddingM),
                            text = stringResource(id = R.string.button_import)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(start = PaddingM, bottom = PaddingM),
                            onClick = {
                                // TODO
                            },
                        ) {
                            Text(
                                modifier = Modifier.padding(PaddingM),
                                text = stringResource(id = R.string.button_export)
                            )
                        }
                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = PaddingM, bottom = PaddingM),
                            onClick = {
                                // TODO
                            },
                        ) {
                            Text(
                                modifier = Modifier.padding(PaddingM),
                                text = stringResource(id = R.string.button_new_wish)
                            )
                        }
                    }
                }
            }
        }
    }
}

val PREVIEW_SCREEN_MODEL = WishlistScreenModel(emptyList())
val PREVIEW_CONTENT_COMPOSABLE: @Composable (ScreenModel) -> Unit = {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = "Preview content"
        )
    }
}