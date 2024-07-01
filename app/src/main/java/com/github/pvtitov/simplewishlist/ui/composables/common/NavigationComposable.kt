package com.github.pvtitov.simplewishlist.ui.composables.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.ui.composables.screens.LoginComposable
import com.github.pvtitov.simplewishlist.ui.composables.screens.WishListComposable
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
            if (isAuthenticated) {
                val dtoState = mainViewModel
                    .userDataState
                    .collectAsStateWithLifecycle()

                val firstData = dtoState.value.data.firstOrNull() //TODO for testing
                if (firstData != null) {
                    HostComposable(
                        screenModel = WishlistScreenModel(firstData.wishList)
                    ) { scr, mod ->
                        when (scr) {
                            is WishlistScreenModel -> WishListComposable(firstData.wishList, mod)
                            else -> Unit
                        }
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