package com.github.pvtitov.simplewishlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.ViewModelInitializer
import androidx.lifecycle.viewmodel.plus
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.pvtitov.simplewishlist.ui.composable.common.NavigationComposable
import com.github.pvtitov.simplewishlist.ui.old.viewmodel.MainViewModel
import com.github.pvtitov.simplewishlist.ui.viewmodel.NavViewModel
import com.github.pvtitov.simplewishlist.utils.DI
import com.github.pvtitov.simplewishlist.utils.FeatureFlags
import com.github.pvtitov.simplewishlist.ui.old.composable.common.NavigationComposable as OldNavigationComposable

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    init {
        DI.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FeatureFlags.NEW_DESIGN) {
            enableEdgeToEdge()
        }

        setContent {
            if (FeatureFlags.NEW_DESIGN) {
                NavigationComposable()
            } else {
                OldNavigationComposable(
                    viewModel = mainViewModel,
                    modifier = Modifier.fillMaxSize(),
                    coroutineScope = lifecycleScope
                )
            }
        }
    }
}