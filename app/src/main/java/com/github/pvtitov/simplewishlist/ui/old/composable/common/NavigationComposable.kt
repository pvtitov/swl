package com.github.pvtitov.simplewishlist.ui.old.composable.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.old.composable.screen.*
import com.github.pvtitov.simplewishlist.ui.old.model.AddFriendScreen
import com.github.pvtitov.simplewishlist.ui.old.model.DeleteWishScreen
import com.github.pvtitov.simplewishlist.ui.old.model.EditWishScreen
import com.github.pvtitov.simplewishlist.ui.old.model.NewWishScreen
import com.github.pvtitov.simplewishlist.ui.old.model.UsersScreen
import com.github.pvtitov.simplewishlist.ui.old.model.WishListScreen
import com.github.pvtitov.simplewishlist.ui.old.model.WishScreen
import com.github.pvtitov.simplewishlist.ui.old.theme.AnotherWishListTheme
import com.github.pvtitov.simplewishlist.ui.old.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationComposable(
    viewModel: MainViewModel,
    modifier: Modifier,
    coroutineScope: CoroutineScope
) {
    AnotherWishListTheme {
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