package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.composable.item.FriendItem
import androidx.compose.runtime.State
import com.github.pvtitov.simplewishlist.ui.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Preview
@Composable
fun FriendsScreen(
    navigation: Navigation = PREVIEW_NAVIGATION
) {
    val friendsState = stubFriends().collectAsStateWithLifecycle()

    val paddingM = dimensionResource(R.dimen.padding_m)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM),
        horizontalAlignment = Alignment.Start
    ) {
        friendsState.value.forEach { friendIndex ->
            item {
                FriendItem(
                    friendIndex = friendIndex,
                    navigation = navigation,
                    url = if (friendIndex % 2 == 0) null else "https://img.magnific.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80"
                )
            }
        }
    }
}

private fun stubFriends(): StateFlow<List<Int>> {
    return MutableStateFlow((0..20).toList()).asStateFlow()
}

private val PREVIEW_NAVIGATION = object : Navigation {
    override val currentScreenState: State<Screen>
        get() = mutableStateOf(Screen.Login)

    override val isBackAvailable: Boolean
        get() = false

    override fun open(screen: Screen) {
    }

    override fun back() {
    }
}