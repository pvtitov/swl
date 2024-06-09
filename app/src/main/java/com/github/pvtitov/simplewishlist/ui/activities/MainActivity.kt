package com.github.pvtitov.simplewishlist.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.ui.theme.SimpleWishListTheme
import com.github.pvtitov.simplewishlist.ui.viewmodels.MainViewModel
import com.github.pvtitov.simplewishlist.utils.DI

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val manualRepository: Repository = DI.getAccountDataSource(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.setManualAccountDataSource(manualRepository)

        setContent {
            SimpleWishListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box {
                        LazyColumn {
                            item {
                                WishListTestOutput(mainViewModel)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    mainViewModel.download()
                                },
                            ) {
                                Text(text = "Import")
                            }
                            FloatingActionButton(
                                onClick = {
                                    mainViewModel.upload()
                                },
                            ) {
                                Text(text = "Export")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WishListTestOutput(mainViewModel: MainViewModel) {
        val userState by mainViewModel.userDataState
            .collectAsStateWithLifecycle()
        val errorState by mainViewModel.errorState.collectAsStateWithLifecycle()
        val currentUserData =
            userState.data.firstOrNull()// { it.user == mainViewModel.getMyLogin() }
                ?: return
        val stringBuilder = StringBuilder(currentUserData.user.name)
        currentUserData.wishList.forEach { wish ->
            stringBuilder.append("\n")
            if (wish.title.isNotEmpty()) {
                stringBuilder.append("\n").append(wish.title)
            }
            if (wish.description?.isNotEmpty() == true) {
                stringBuilder.append("\n").append(wish.description)
            }
            if (wish.wishUrl?.isNotEmpty() == true) {
                stringBuilder.append("\n").append(wish.wishUrl)
            }
        }
        if (errorState != MainViewModel.NO_ERROR) {
            stringBuilder.clear()
            stringBuilder.append(errorState.message)
        }
        Text(text = stringBuilder.toString())
    }
}