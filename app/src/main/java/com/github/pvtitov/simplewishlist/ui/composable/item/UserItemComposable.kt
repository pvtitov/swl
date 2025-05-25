package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.composable.element.ImageComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun UserItemComposable(
    user: User = PREVEIW_USER,
    viewModel: MainViewModel = MainViewModel(),
    isFirst: Boolean = false,
    coroutineScope: CoroutineScope
) {
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val paddingL = dimensionResource(id = R.dimen.padding_l)
    val imageSize = dimensionResource(id = R.dimen.user_item_image_size)
    val topPadding = if (isFirst) paddingL else paddingM

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingM, top = topPadding, end = paddingM)
            .clickable {
                viewModel.onClickUser(user)
            }
    ) {
        Row(
            modifier = Modifier.padding(paddingM)
        ) {
            ImageComposable(
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize)
                    .clip(CircleShape),
                imageUrl = user.imageUrl,
                loadingPlaceholderId = R.drawable.ic_placeholder_24,
                failurePlaceholderId = R.drawable.ic_placeholder_24,
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = paddingM)
            ) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = user.name ?: user.login,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier
                        .padding(top = paddingS),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = user.login,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private val PREVEIW_USER = User(
    login = "user_login",
    name = "user_name",
    imageUrl = ""
)