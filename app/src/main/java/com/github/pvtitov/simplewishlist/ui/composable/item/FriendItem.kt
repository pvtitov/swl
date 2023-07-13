package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.github.pvtitov.simplewishlist.ui.model.Friend

@Composable
fun FriendItem(
    friend: Friend,
    onClick: () -> Unit,
) {
    val avatarSizeM = dimensionResource(R.dimen.avatar_m)
    val paddingM = dimensionResource(R.dimen.padding_m)

    val (name, login, _, imageUrl) = friend

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        AsyncImage(
            model = imageUrl,
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
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = login,
            )
        }
    }
}

@Preview
@Composable
fun FriendItemPreview() {
    FriendItem(
        friend = Friend("name", "login@mail.com", 0, null),
        onClick = {}
    )
}