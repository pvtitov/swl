package com.github.pvtitov.simplewishlist.ui.composable.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.composable.element.ImageComposable
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun UserItemComposable(
    user: User = PREVEIW_USER,
    viewModel: MainViewModel = MainViewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_xs))
            .clickable {
                viewModel.onClickUser(user)
            }
    ) {
        val paddingS = dimensionResource(id = R.dimen.padding_s)
        val paddingM = dimensionResource(id = R.dimen.padding_m)
        val imageSize = dimensionResource(id = R.dimen.user_item_image_size)

        Row(
            modifier = Modifier.padding(paddingM)
        ) {
            ImageComposable(
                modifier = Modifier
                    .width(imageSize)
                    .height(imageSize),
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
                    text = user.name,
                )
                Text(
                    modifier = Modifier
                        .padding(top = paddingS),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = user.login
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