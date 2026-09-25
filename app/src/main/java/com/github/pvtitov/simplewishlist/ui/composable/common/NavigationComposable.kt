package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    val newWishContentDescription = stringResource(R.string.new_wish_title)
    val newFriendContentDescription = stringResource(R.string.new_friend_title)
    val myWishesTitle = stringResource(R.string.my_wishes_title)
    val friendsTitle = stringResource(R.string.friends_title)
    val saveButton = stringResource(R.string.save_button)

    val paddingM = dimensionResource(R.dimen.padding_m)

    val navigationBarDestinations = remember { listOf(Screen.MyWishes, Screen.Friends) }
    val navigationBarIcons = remember { listOf(R.drawable.ic_heart_24, R.drawable.ic_friends_24) }
    val navigationBarTitles = remember { listOf(myWishesTitle, friendsTitle) }

    val currentScreen by navigation.currentScreenState

    val isBottomBarAvailable = currentScreen is Screen.MyWishes || currentScreen is Screen.Friends

    AwlTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = {
                        val title = when (currentScreen) {
                            is Screen.Friends -> stringResource(R.string.friends_title)
                            is Screen.MyWishes -> stringResource(R.string.my_wishes_title)
                            is Screen.NewFriend -> stringResource(R.string.new_friend_title)
                            is Screen.NewWish -> stringResource(R.string.new_wish_title)
                            else -> null
                        }
                        if (title != null) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineSmall,
                                softWrap = false
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = paddingM),
                    navigationIcon = {
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
                    },
                    actions = {
                        if (currentScreen is Screen.NewWish || currentScreen is Screen.NewFriend) {
                            Button(
                                onClick = {
                                    // TODO save
                                    navigation.back()
                                }
                            ) {
                                Text(saveButton)
                            }
                        } else if (currentScreen is Screen.MyWishes || currentScreen is Screen.Friends) {
                            Avatar()
                        }
                    }
                )
            },
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
            },
            floatingActionButton = {
                if (currentScreen is Screen.MyWishes || currentScreen is Screen.Friends) {
                    FloatingActionButton(
                        onClick = {
                            navigation.open(
                                if (currentScreen is Screen.Friends) {
                                    Screen.NewFriend
                                } else {
                                    Screen.NewWish
                                }
                            )
                        },
                        modifier = Modifier.padding(paddingM)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add_24),
                            contentDescription = if (currentScreen is Screen.Friends) {
                                newFriendContentDescription
                            } else {
                                newWishContentDescription
                            }
                        )
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                verticalArrangement = Arrangement.Bottom,
            ) {
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
                        is Screen.NewFriend -> NewFriendScreen()
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

@Composable
fun Avatar() {
    val borderWidth = dimensionResource(R.dimen.border_width)
    val avatarM = dimensionResource(R.dimen.avatar_m)

    Image(
        painter = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(avatarM)
            .border(
                BorderStroke(borderWidth, MaterialTheme.colorScheme.primary),
                CircleShape
            )
            .padding(borderWidth)
            .clip(CircleShape)
    )
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
                                || currentScreen is Screen.NewWish
                                || currentScreen is Screen.NewFriend
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