package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.github.pvtitov.simplewishlist.ui.composable.item.WishItem
import com.github.pvtitov.simplewishlist.ui.model.Friend
import com.github.pvtitov.simplewishlist.ui.model.Wish

@Composable
fun WishesScreen(
    friend: Friend,
    wishes: List<Wish>,
    onWishClicked: (Int) -> Unit,
) {
    val paddingM = dimensionResource(R.dimen.padding_m)
    val avatarSizeM = dimensionResource(R.dimen.avatar_m)

    LazyColumn(
        contentPadding = PaddingValues(paddingM),
        verticalArrangement = Arrangement.spacedBy(paddingM)
    ) {
        item {
            Row {
                AsyncImage(
                    model = null, // TODO
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
                        text = friend.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = friend.login,
                    )
                }
            }
        }
        wishes.forEachIndexed { index, wish ->
            item {
                WishItem(
                    wish = wish,
                    onClick = { onWishClicked(index) }
                )
            }
        }
    }
}

@Preview
@Composable
fun WishesScreenPreview() {
    WishesScreen(
        Friend("name", "login", 0, null),
        listOf(Wish(null, null, null, null)),
        {}
    )
}