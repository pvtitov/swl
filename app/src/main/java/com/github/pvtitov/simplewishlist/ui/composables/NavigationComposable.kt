package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    SimpleWishListTheme {
        Surface(
            modifier = modifier
        ) {
            if (credentials != null) {
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
                Box(
                    modifier = modifier
                ) {
                    LoginComposable(
                        loginViewModel = loginViewModel,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        }
    }
}