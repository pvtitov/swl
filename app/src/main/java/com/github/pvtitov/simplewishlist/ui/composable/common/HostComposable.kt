package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.element.IndicatorComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview
@Composable
fun HostComposable(
    viewModel: MainViewModel = MainViewModel(),
    contentComposable: @Composable () -> Unit = PREVIEW_CONTENT_COMPOSABLE,
) {
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingL = dimensionResource(id = R.dimen.padding_l)

    var isControlsVisible by remember {
        mutableStateOf(true)
    }

    val currentLogin by viewModel.currentLoginFlow.collectAsStateWithLifecycle("")
    val isDataUpdated by viewModel.isWishListUpdatedState.collectAsStateWithLifecycle(false)

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope { Dispatchers.IO }

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        isControlsVisible = false

                        do {
                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                        } while (event.changes.any { it.pressed })

                        isControlsVisible = true
                    }
                }
        ) {
            contentComposable()
        }
        Row {
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = paddingL, top = paddingL)
                    .align(Alignment.CenterVertically)
            ) {
                IndicatorComposable(isIgnited = isDataUpdated)
            }
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = paddingL, top = paddingL)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = currentLogin ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                        )
                        .padding(paddingS)
                )
            }
        }
        AnimatedVisibility(
            visible = isControlsVisible,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = paddingL, end = paddingL)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.onClickLogin()
                    }
                },
            ) {
                Text(
                    modifier = Modifier.padding(paddingL),
                    text = stringResource(id = R.string.host_button_login)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = paddingL, bottom = paddingL)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickDownload()
                        }
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(paddingL),
                        text = stringResource(id = R.string.host_button_import)
                    )
                }
            }
            AnimatedVisibility(
                visible = isControlsVisible,
                modifier = Modifier
                    .padding(start = paddingL, bottom = paddingL)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickUpload(context)
                        }
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(paddingL),
                        text = stringResource(id = R.string.host_button_export)
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isControlsVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickNewWish()
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = paddingL, end = paddingL)
                        .align(Alignment.End)
                ) {
                    Text(
                        modifier = Modifier.padding(paddingL),
                        text = stringResource(id = R.string.host_button_new_wish)
                    )
                }
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickUsers()
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = paddingL, end = paddingL)
                        .align(Alignment.End)
                ) {
                    Text(
                        modifier = Modifier.padding(paddingL),
                        text = stringResource(id = R.string.host_button_friends)
                    )
                }
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onClickWishList()
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = paddingL, end = paddingL)
                        .align(Alignment.End)
                ) {
                    Text(
                        modifier = Modifier.padding(paddingL),
                        text = stringResource(id = R.string.host_button_wish_list)
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {

                }
            }
        }
    }
}

val PREVIEW_CONTENT_COMPOSABLE: @Composable () -> Unit = {
//    WishListComposable()
}
