package com.github.pvtitov.simplewishlist.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.pvtitov.simplewishlist.ui.composable.common.NavigationComposable
import com.github.pvtitov.simplewishlist.utils.DI

class MainActivity : ComponentActivity() {
    init {
        DI.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NavigationComposable()
        }
    }
}