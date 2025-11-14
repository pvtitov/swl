package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.composable.screen.*
import com.github.pvtitov.simplewishlist.ui.model.*
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationComposable(
    viewModel: MainViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope
) {
    SimpleWishListTheme {
        Surface(
            modifier = modifier
        ) {
            val screenModel by viewModel.currentScreenState.collectAsStateWithLifecycle()

            HostComposable(
                viewModel = viewModel
            ) {
                when (val screen = screenModel) {
                    is UsersScreen -> {
                        UserListComposable(screen.users, viewModel, coroutineScope)
                    }

                    is AddFriendScreen -> {
                        NewFriendComposable(viewModel, coroutineScope)
                    }

                    is WishListScreen -> {
                        WishListComposable(
                            screen.wishList?.wishes
                                ?: emptyList(),
                            viewModel,
                            coroutineScope = coroutineScope
                        )
                    }

                    is WishScreen -> {
                        WishComposable(screen.wish, viewModel, coroutineScope)
                    }

                    is EditWishScreen -> {
                        EditWishComposable(screen.wish, viewModel, coroutineScope)
                    }

                    is DeleteWishScreen -> {
                        DeleteWishComposable(screen.wish, viewModel, coroutineScope)
                    }

                    is NewWishScreen -> {
                        NewWishComposable(viewModel, coroutineScope)
                    }

                    else -> Unit
                }
            }
        }
    }
}