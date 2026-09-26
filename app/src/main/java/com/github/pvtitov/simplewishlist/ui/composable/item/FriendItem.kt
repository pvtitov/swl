package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.ui.composable.common.Navigation
import com.github.pvtitov.simplewishlist.ui.model.Screen

@Preview
@Composable
fun FriendItem(
    friendIndex: Int = PREVIEW_FRIEND_INDEX,
    navigation: Navigation = PREVIEW_NAVIGATION,
    url: String? = null,
) {
    val avatarSizeM = dimensionResource(R.dimen.avatar_m)
    val paddingM = dimensionResource(R.dimen.padding_m)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigation.open(Screen.Wishes(friendIndex))
            }
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(avatarSizeM)
                .clip(CircleShape),
            placeholder = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
            error = ColorPainter(MaterialTheme.colorScheme.primaryContainer),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.padding(start = paddingM)
        ) {
            Text(
                text = "My friend's name",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "mail@example.com",
            )
        }
    }
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

private const val PREVIEW_FRIEND_INDEX = 0