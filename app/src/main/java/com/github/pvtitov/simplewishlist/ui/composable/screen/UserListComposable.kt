package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.composable.item.UserItemComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun UserListComposable(
    users: List<User> = PREVIEW_USER_LIST,
    viewModel: MainViewModel = MainViewModel(),
    coroutineScope: CoroutineScope
) {
    LazyColumn {
        users.forEachIndexed { index, user ->
            item {
                UserItemComposable(user, viewModel, index == 0)
            }
        }
    }
}

private val PREVIEW_USER_LIST = List(size = 4) { i ->
    User(
        login = "user_login_$i",
        name = "$i user name",
        imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg"
    )
}