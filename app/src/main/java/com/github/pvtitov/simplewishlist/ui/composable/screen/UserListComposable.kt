package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.composable.item.UserItemComposable

@Preview
@Composable
fun UserListComposable(
    users: List<User> = PREVIEW_USER_LIST,
) {
    LazyColumn {
        users.forEach { user ->
            item {
                UserItemComposable(user)
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