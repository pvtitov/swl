package com.github.pvtitov.simplewishlist.ui.composable.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.screen.*
import com.github.pvtitov.simplewishlist.ui.model.Screen
import com.github.pvtitov.simplewishlist.ui.theme.AwlTheme

@Preview
@Composable
fun NavigationComposable() {
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
                                            || currentScreen is Screen.Login
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
                verticalArrangement = Arrangement.Bottom,
            ) {
                val currentScreen = navigation.currentScreenState.value

                if (navigation.isBackAvailable) {
                    IconButton(
                        onClick = { navigation.back() },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_24),
                            contentDescription = "Back"
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
                        is Screen.Login -> LoginScreen()
                        is Screen.MyWishes -> MyWishesScreen()
                        is Screen.NewWish -> NewWishScreen()
                        is Screen.MyWish -> MyWishScreen(currentScreen.wishIndex)
                        is Screen.Friends -> FriendsScreen()
                        is Screen.Wishes -> WishesScreen(currentScreen.friendIndex)
                        is Screen.Wish -> WishScreen(currentScreen.friendIndex, currentScreen.wishIndex)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SelectableButton(
                        resId = R.drawable.ic_heart_24,
                        contentDescription = "Login",
                        screenRepresented = Screen.Login,
                        currentScreen = currentScreen,
                        navigation = navigation,
                    )
                    SelectableButton(
                        resId = R.drawable.ic_heart_24,
                        contentDescription = "MyWishes",
                        screenRepresented = Screen.MyWishes,
                        currentScreen = currentScreen,
                        navigation = navigation,
                    )
                    SelectableButton(
                        resId = R.drawable.ic_heart_24,
                        contentDescription = "NewWish",
                        screenRepresented = Screen.NewWish,
                        currentScreen = currentScreen,
                        navigation = navigation,
                    )
                    SelectableButton(
                        resId = R.drawable.ic_heart_24,
                        contentDescription = "Friends",
                        screenRepresented = Screen.Friends,
                        currentScreen = currentScreen,
                        navigation = navigation,
                    )
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
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = if (currentScreen == screenRepresented) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        IconButton(
            onClick = { navigation.open(screenRepresented) },
        ) {
            Icon(
                painter = painterResource(resId),
                contentDescription = contentDescription
            )
        }
    }
}

interface Navigation {
    val currentScreenState: State<Screen>
    val isBackAvailable: Boolean
    fun open(screen: Screen)
    fun back()
}