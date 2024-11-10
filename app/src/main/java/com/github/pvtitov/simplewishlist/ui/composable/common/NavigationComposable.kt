package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.composable.screen.DeleteWishComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.EditWishComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.LoginComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.NewWishComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.UserListComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.WishComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.WishListComposable
import com.github.pvtitov.simplewishlist.ui.model.DeleteWishScreen
import com.github.pvtitov.simplewishlist.ui.model.EditWishScreen
import com.github.pvtitov.simplewishlist.ui.model.LoginScreen
import com.github.pvtitov.simplewishlist.ui.model.NewWishScreen
import com.github.pvtitov.simplewishlist.ui.model.UsersScreen
import com.github.pvtitov.simplewishlist.ui.model.WishScreen
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreen
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Composable
fun NavigationComposable(
    viewModel: MainViewModel,
    modifier: Modifier
) {
    SimpleWishListTheme {
        Surface(
            modifier = modifier
        ) {
            val screenModel by viewModel.currentScreenState.collectAsStateWithLifecycle()

            if (screenModel == LoginScreen) {
                LoginComposable(
                    viewModel = viewModel
                )
            } else {
                HostComposable(
                    viewModel = viewModel
                ) {
                    when (val screen = screenModel) {
                        is UsersScreen ->
                            UserListComposable(screen.users, viewModel)

                        is WishlistScreen -> {
                            WishListComposable(
                                screen.userData?.wishList
                                    ?: emptyList(),
                                viewModel
                            )
                        }

                        is WishScreen -> {
                            WishComposable(screen.wish, viewModel)
                        }

                        is EditWishScreen -> {
                            EditWishComposable(screen.wish, viewModel)
                        }

                        is DeleteWishScreen -> {
                            DeleteWishComposable(screen.wish, viewModel)
                        }

                        is NewWishScreen -> {
                            NewWishComposable(viewModel)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}