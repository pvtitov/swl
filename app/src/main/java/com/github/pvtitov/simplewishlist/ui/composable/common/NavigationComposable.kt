package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.composable.screen.LoginComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.UserListComposable
import com.github.pvtitov.simplewishlist.ui.composable.screen.WishListComposable
import com.github.pvtitov.simplewishlist.ui.model.LoginScreenModel
import com.github.pvtitov.simplewishlist.ui.model.UsersScreenModel
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreenModel
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
            val scr = screenModel

            if (scr == LoginScreenModel) {
                LoginComposable(
                    viewModel = viewModel
                )
            } else {
                HostComposable(
                    viewModel = viewModel
                ) {
                    when (scr) {
                        is UsersScreenModel -> UserListComposable(scr.users)
                        is WishlistScreenModel -> WishListComposable(scr.wishlist)
                        else -> Unit
                    }
                }
            }
        }
    }
}