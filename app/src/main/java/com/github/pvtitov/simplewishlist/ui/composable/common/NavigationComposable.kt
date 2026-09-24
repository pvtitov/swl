package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.screen.*
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.theme.AwlTheme

@Preview
@Composable
fun NavigationComposable(
    modifier: Modifier = Modifier
) {
    val currentScreenState = rememberSaveable { mutableStateOf<Screen>(Screen.Login) }
    val screensBackStackState = rememberSaveable { mutableStateListOf<Screen>() }

    val navigation: Navigation = createNavigation(currentScreenState, screensBackStackState)

    val backContentDescription = stringResource(R.string.back_content_description)
    val myWishesTitle = stringResource(R.string.my_wishes_title)
    val friendsTitle = stringResource(R.string.friends_title)

    val navigationBarDestinations = remember { listOf(Screen.MyWishes, Screen.Friends) }
    val navigationBarIcons = remember { listOf(R.drawable.ic_heart_24, R.drawable.ic_friends_24) }
    val navigationBarTitles = remember { listOf(myWishesTitle, friendsTitle) }

    val currentScreen by navigation.currentScreenState

    val isBottomBarAvailable = currentScreen is Screen.MyWishes || currentScreen is Screen.Friends

    AwlTheme {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                if (isBottomBarAvailable) {
                    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                        navigationBarDestinations.forEachIndexed { index, screen ->
                            val title = navigationBarTitles[index]
                            val iconRes = navigationBarIcons[index]
                            NavigationBarItem(
                                selected = screen == currentScreen,
                                onClick = {
                                    navigation.open(screen)
                                },
                                icon = {
                                    Icon(
                                        painterResource(iconRes),
                                        contentDescription = title
                                    )
                                },
                                label = { Text(title) }
                            )
                        }
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (navigation.isBackAvailable) {
                    IconButton(
                        onClick = { navigation.back() },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_24),
                            contentDescription = backContentDescription
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1F),
                    contentAlignment = Alignment.Center
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Login -> LoginScreen(navigation)
                        is Screen.MyWishes -> MyWishesScreen()
                        is Screen.NewWish -> NewWishScreen()
                        is Screen.MyWish -> MyWishScreen(screen.wishIndex)
                        is Screen.Friends -> FriendsScreen()
                        is Screen.Wishes -> WishesScreen(screen.friendIndex)
                        is Screen.Wish -> WishScreen(screen.friendIndex, screen.wishIndex)
                    }
                }
            }
        }
    }
}

fun createNavigation(
    currentScreenState: MutableState<Screen>,
    screensBackStackState: SnapshotStateList<Screen>
): Navigation {
    return object : Navigation {
        override val currentScreenState: State<Screen>
            get() = currentScreenState

        override val isBackAvailable: Boolean
            get() {
                val currentScreen = currentScreenState.value
                return screensBackStackState.isNotEmpty() && (
                        currentScreen is Screen.Wishes
                                || currentScreen is Screen.MyWish
                                || currentScreen is Screen.Wish
                        )
            }

        override fun open(screen: Screen) {
            saveToBackStack(currentScreenState.value)
            currentScreenState.value = screen
        }

        override fun back() {
            val previousScreen = screensBackStackState.removeLastOrNull()
            if (previousScreen != null) currentScreenState.value = previousScreen
        }

        private fun saveToBackStack(screen: Screen) {
            when (screen) {
                is Screen.Friends,
                is Screen.MyWishes,
                is Screen.Wishes -> screensBackStackState.add(screen)

                else -> screensBackStackState.clear()
            }
        }
    }
}

interface Navigation {
    val currentScreenState: State<Screen>
    val isBackAvailable: Boolean
    fun open(screen: Screen)
    fun back()
}