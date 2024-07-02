package com.github.pvtitov.simplewishlist.ui.composables.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.composables.screens.LoginComposable
import com.github.pvtitov.simplewishlist.ui.composables.screens.UserListComposable
import com.github.pvtitov.simplewishlist.ui.composables.screens.WishListComposable
import com.github.pvtitov.simplewishlist.ui.model.UsersScreenModel
import com.github.pvtitov.simplewishlist.ui.model.WishlistScreenModel
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme
import com.github.pvtitov.simplewishlist.ui.viewmodels.LoginViewModel
import com.github.pvtitov.simplewishlist.ui.viewmodels.MainViewModel

@Composable
fun NavigationComposable(
    loginViewModel: LoginViewModel,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {
    val credentials by loginViewModel.credentialsState.collectAsStateWithLifecycle()
    val isAuthenticated = credentials != null

    SimpleWishListTheme {
        Surface(
            modifier = modifier
        ) {
            val screenModel by mainViewModel.currentScreenState.collectAsStateWithLifecycle()

            if (isAuthenticated) {
                HostComposable(
                    screenModel = screenModel
                ) {
                    when (val scr = screenModel) {
                        is UsersScreenModel -> UserListComposable(scr.users)
                        is WishlistScreenModel -> WishListComposable(scr.wishlist)
                        else -> Unit
                    }
                }
            } else {
                LoginComposable(
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}