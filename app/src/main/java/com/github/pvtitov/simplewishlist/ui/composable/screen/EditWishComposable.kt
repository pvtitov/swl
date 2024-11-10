package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun EditWishComposable(
    wish: Wish? = PREVIEW_WISH,
    viewModel: MainViewModel = MainViewModel(),
) {
    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingL = dimensionResource(id = R.dimen.padding_l)

    val wishTitle = rememberSaveable {
        mutableStateOf(wish?.title ?: "")
    }
    val wishDescription = rememberSaveable {
        mutableStateOf(wish?.description ?: "")
    }
    val wishImageUrl = rememberSaveable {
        mutableStateOf(wish?.imageUrl ?: "")
    }
    val wishUrl = rememberSaveable {
        mutableStateOf(wish?.wishUrl ?: "")
    }

    Card {
        Column(
            modifier = Modifier
                .padding(paddingL)
                .fillMaxSize(),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = wishTitle.value,
                onValueChange = { value -> wishTitle.value = value },
                label = {
                    Text(text = stringResource(id = R.string.wish_field_title))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = wishDescription.value,
                onValueChange = { value -> wishDescription.value = value },
                label = {
                    Text(text = stringResource(id = R.string.wish_field_description))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = wishImageUrl.value,
                onValueChange = { value -> wishImageUrl.value = value },
                label = {
                    Text(text = stringResource(id = R.string.wish_field_image_url))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = wishUrl.value,
                onValueChange = { value -> wishUrl.value = value },
                label = {
                    Text(text = stringResource(id = R.string.wish_field_url))
                }
            )
            Button(
                onClick = {
                    viewModel.onClickSaveNewWish(
                        wish,
                        Wish(
                            title = wishTitle.value,
                            description = wishDescription.value,
                            imageUrl = wishImageUrl.value,
                            wishUrl = wishUrl.value
                        )
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.wish_button_save))
            }
        }
    }
}

private val PREVIEW_WISH = Wish(
    title = "Title",
    description = "Description",
    imageUrl = "https://t3.ftcdn.net/jpg/01/51/88/34/360_F_151883482_k4sHBdux0c2v7syFocRIoFkOQTR7Evkp.jpg",
    wishUrl = "https://google.com"
)