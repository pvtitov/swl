package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun NewFriendComposable(
    viewModel: MainViewModel = MainViewModel(),
    coroutineScope: CoroutineScope
) {
    val friendName = rememberSaveable {
        mutableStateOf("")
    }
    val friendLogin = rememberSaveable {
        mutableStateOf("")
    }

    val paddingS = dimensionResource(id = R.dimen.padding_s)
    val paddingL = dimensionResource(id = R.dimen.padding_l)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(paddingL)
                .align(Alignment.Center),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = friendName.value,
                onValueChange = { value -> friendName.value = value },
                label = {
                    Text(
                        text = stringResource(id = R.string.friend_field_name),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = paddingS),
                value = friendLogin.value,
                onValueChange = { value -> friendLogin.value = value },
                label = {
                    Text(
                        text = stringResource(id = R.string.friend_field_login),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.onClickSaveNewFriend(
                            User(
                                login = friendLogin.value,
                                name = friendName.value,
                            )
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = paddingL)
            ) {
                Text(
                    text = stringResource(id = R.string.button_save),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}