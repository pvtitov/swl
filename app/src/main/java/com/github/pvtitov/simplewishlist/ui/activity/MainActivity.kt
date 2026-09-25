package com.github.pvtitov.simplewishlist.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.github.pvtitov.simplewishlist.ui.composable.common.NavigationComposable
import com.github.pvtitov.simplewishlist.ui.old.viewmodel.MainViewModel
import com.github.pvtitov.simplewishlist.utils.DI
import com.github.pvtitov.simplewishlist.utils.FeatureFlags
import com.github.pvtitov.simplewishlist.ui.old.composable.common.NavigationComposable as OldNavigationComposable

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    init {
        DI.init(this)
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