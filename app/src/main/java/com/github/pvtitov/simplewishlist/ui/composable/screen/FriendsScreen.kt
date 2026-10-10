package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.item.FriendItem
import com.github.pvtitov.simplewishlist.ui.model.Friend

@Composable
fun FriendsScreen(
    friends: List<Friend>,
    onFriendClicked: (Int) -> Unit,
    onTopBarTitle: (String) -> Unit
) {
    val paddingM = dimensionResource(R.dimen.padding_m)

    val topBarTitle = stringResource(R.string.friends_title)
    onTopBarTitle(topBarTitle)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM),
        horizontalAlignment = Alignment.Start
    ) {
        friends.forEachIndexed { index, friend ->
            item {
                FriendItem(
                    friend = friend,
                    onClick = { onFriendClicked(index) }
                )
            }
        }
    }
}

@Preview
@Composable
fun FriendsScreenPreview() {
    FriendsScreen(
        friends = listOf(Friend("name", "login", 0, null)),
        onFriendClicked = {},
        onTopBarTitle = {}
    )
}