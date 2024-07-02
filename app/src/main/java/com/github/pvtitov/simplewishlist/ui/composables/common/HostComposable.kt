package com.github.pvtitov.simplewishlist.ui.composables.common

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composables.elements.IndicatorComposable
import com.github.pvtitov.simplewishlist.ui.composables.screens.WishListComposable
import com.github.pvtitov.simplewishlist.ui.model.ScreenModel
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreenModel


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun HostComposable(
    screenModel: ScreenModel = PREVIEW_SCREEN_MODEL,
    contentComposable: @Composable () -> Unit = PREVIEW_CONTENT_COMPOSABLE,
) {
    val padding = dimensionResource(id = R.dimen.padding_l)

    var isControlsVisible by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> isControlsVisible = false

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> isControlsVisible = true
                }
                true
            }
    ) {
        contentComposable()
        Row {
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier.padding(start = padding, top = padding)
            ) {
                IndicatorComposable()
            }
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier.padding(start = padding, top = padding)
            ) {
                Text(
                    text = "test_login",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.white),
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                        )
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = padding, end = padding)
            ) {
                FloatingActionButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(padding),
                        text = stringResource(id = R.string.host_button_login)
                    )
                }
            }
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = padding, end = padding)
            ) {
                FloatingActionButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(padding),
                        text = stringResource(id = R.string.host_button_friends)
                    )
                }
            }

        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = padding, bottom = padding)
            ) {
                FloatingActionButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(padding),
                        text = stringResource(id = R.string.host_button_import)
                    )
                }
            }
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = padding, bottom = padding)
            ) {
                FloatingActionButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(padding),
                        text = stringResource(id = R.string.host_button_export)
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isControlsVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = padding, bottom = padding)
        ) {
            FloatingActionButton(
                onClick = {
                    // TODO
                },
            ) {
                Text(
                    modifier = Modifier.padding(padding),
                    text = stringResource(id = R.string.host_button_new_wish)
                )
            }
        }
    }
}

val PREVIEW_SCREEN_MODEL = WishlistScreenModel(emptyList())
val PREVIEW_CONTENT_COMPOSABLE: @Composable () -> Unit = {
    WishListComposable()
}
