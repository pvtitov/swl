package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.theme.PaddingM
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme

@Preview
@Composable
fun ScreenContainer(
    contentComposable: @Composable (ScreenModel) -> Unit = {}
) {
    SimpleWishListTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                contentComposable
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                ) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.End)
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
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.End)
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
                }
            }
        }
    }
}