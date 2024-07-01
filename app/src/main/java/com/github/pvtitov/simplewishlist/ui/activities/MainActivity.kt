package com.github.pvtitov.simplewishlist.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.ui.composables.common.NavigationComposable
import com.github.pvtitov.simplewishlist.ui.viewmodels.LoginViewModel
import com.github.pvtitov.simplewishlist.ui.viewmodels.MainViewModel
import com.github.pvtitov.simplewishlist.utils.DI

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val manualRepository: Repository = DI.getAccountDataSource(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.setManualAccountDataSource(manualRepository)

        setContent {
            NavigationComposable(
                loginViewModel = loginViewModel,
                mainViewModel = mainViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}