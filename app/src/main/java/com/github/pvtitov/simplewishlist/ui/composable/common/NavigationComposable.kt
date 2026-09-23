package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.screen.*
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.theme.AwlTheme

@Preview
@Composable
fun NavigationComposable() {
    val paddingM = dimensionResource(R.dimen.padding_m)
    val backContentDescription = stringResource(R.string.back_content_description)
    val myWishesTitle = stringResource(R.string.my_wishes_title)
    val friendsTitle = stringResource(R.string.friends_title)

    AwlTheme {
        Surface {
            val navigation: Navigation = remember {
                object : Navigation {
                    private val _currentScreenState = mutableStateOf<Screen>(Screen.Login)
                    private val _screensBackStackState = mutableStateListOf<Screen>()

                    override val currentScreenState: State<Screen>
                        get() = _currentScreenState

                    override val isBackAvailable: Boolean
                        get() {
                            val currentScreen = _currentScreenState.value
                            return _screensBackStackState.isNotEmpty() && (
                                    currentScreen is Screen.Wishes
                                            || currentScreen is Screen.MyWish
                                            || currentScreen is Screen.Wish
                                    )
                        }

                    override fun open(screen: Screen) {
                        saveToBackStack(currentScreenState.value)
                        _currentScreenState.value = screen
                    }

                    override fun back() {
                        val previousScreen = _screensBackStackState.removeLastOrNull()
                        if (previousScreen != null) _currentScreenState.value = previousScreen
                    }

                    private fun saveToBackStack(screen: Screen) {
                        when (screen) {
                            is Screen.Friends,
                            is Screen.MyWishes,
                            is Screen.Wishes -> _screensBackStackState.add(screen)

                            else -> _screensBackStackState.clear()
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(paddingM),
                verticalArrangement = Arrangement.Bottom,
            ) {
                val currentScreen = navigation.currentScreenState.value

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
                    when (currentScreen) {
                        is Screen.Login -> LoginScreen(navigation)
                        is Screen.MyWishes -> MyWishesScreen()
                        is Screen.NewWish -> NewWishScreen()
                        is Screen.MyWish -> MyWishScreen(currentScreen.wishIndex)
                        is Screen.Friends -> FriendsScreen()
                        is Screen.Wishes -> WishesScreen(currentScreen.friendIndex)
                        is Screen.Wish -> WishScreen(currentScreen.friendIndex, currentScreen.wishIndex)
                    }
                }

                val isBottomBarAvailable = currentScreen is Screen.MyWishes || currentScreen is Screen.Friends
                if (isBottomBarAvailable) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SelectableButton(
                            resId = R.drawable.ic_heart_24,
                            contentDescription = myWishesTitle,
                            screenRepresented = Screen.MyWishes,
                            currentScreen = currentScreen,
                            navigation = navigation,
                        )
                        SelectableButton(
                            resId = R.drawable.ic_friends_24,
                            contentDescription = friendsTitle,
                            screenRepresented = Screen.Friends,
                            currentScreen = currentScreen,
                            navigation = navigation,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableButton(
    @DrawableRes resId: Int,
    contentDescription: String,
    screenRepresented: Screen,
    currentScreen: Screen,
    navigation: Navigation,
    modifier: Modifier = Modifier
) {
    val paddingM = dimensionResource(R.dimen.padding_m)
    val paddingL = dimensionResource(R.dimen.padding_l)
    val paddingXL = dimensionResource(R.dimen.padding_xl)

    Button(
        onClick = { navigation.open(screenRepresented) },
        modifier = modifier
            .size(paddingXL, paddingL)
            .background(
                color = if (currentScreen == screenRepresented) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(paddingM)
            )
    ) {
        Icon(
            painter = painterResource(resId),
            contentDescription = contentDescription,
            tint = if (currentScreen == screenRepresented) {
                MaterialTheme.colorScheme.primary
            } else {
                LocalContentColor.current
            }
        )
    }
}

interface Navigation {
    val currentScreenState: State<Screen>
    val isBackAvailable: Boolean
    fun open(screen: Screen)
    fun back()
}