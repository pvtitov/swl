package com.github.pvtitov.simplewishlist.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
                    Column {
                        Button(onClick = {
                            mainViewModel.download()
                        }) {
                            Text(text = "Import")
                        }
                        Button(onClick = {
                            mainViewModel.upload()
                        }) {
                            Text(text = "Export")
                        }
                        WishListTestOutput(mainViewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun WishListTestOutput(mainViewModel: MainViewModel) {
        //TODO не работает, не загружается контент
        val userState by mainViewModel.userDataState
            .collectAsStateWithLifecycle()
        val errorState by mainViewModel.errorState.collectAsStateWithLifecycle()
        val currentUserData = userState.data.firstOrNull()// { it.user == mainViewModel.getMyLogin() }
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