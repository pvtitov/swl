package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreenModel

@Preview
@Composable
fun HostComposable(
    screenModel: ScreenModel = PREVIEW_SCREEN_MODEL,
    contentComposable: @Composable (ScreenModel) -> Unit = PREVIEW_CONTENT_COMPOSABLE
) {
    val paddingM = dimensionResource(id = R.dimen.padding_m)

    Box {
        contentComposable(screenModel)
        Column {
            Text(
                modifier = Modifier.padding(paddingM),
                text = "test_login"
            )
            IndicatorComposable(
                modifier = Modifier.padding(start = paddingM)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = paddingM, end = paddingM),
                onClick = {
                    // TODO
                },
            ) {
                Text(
                    modifier = Modifier.padding(paddingM),
                    text = stringResource(id = R.string.host_button_login)
                )
            }
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = paddingM, end = paddingM),
                onClick = {
                    // TODO
                },
            ) {
                Text(
                    modifier = Modifier.padding(paddingM),
                    text = stringResource(id = R.string.host_button_friends)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(start = paddingM, bottom = paddingM),
                onClick = {
                    // TODO
                },
            ) {
                Text(
                    modifier = Modifier.padding(paddingM),
                    text = stringResource(id = R.string.host_button_import)
                )
            }
            FloatingActionButton(
                modifier = Modifier
                    .padding(start = paddingM, bottom = paddingM),
                onClick = {
                    // TODO
                },
            ) {
                Text(
                    modifier = Modifier.padding(paddingM),
                    text = stringResource(id = R.string.host_button_export)
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = paddingM, bottom = paddingM),
            onClick = {
                // TODO
            },
        ) {
            Text(
                modifier = Modifier.padding(paddingM),
                text = stringResource(id = R.string.host_button_new_wish)
            )
        }
    }
}

val PREVIEW_SCREEN_MODEL = WishlistScreenModel(emptyList())
val PREVIEW_CONTENT_COMPOSABLE: @Composable (ScreenModel) -> Unit = {
    WishListComposable()
}