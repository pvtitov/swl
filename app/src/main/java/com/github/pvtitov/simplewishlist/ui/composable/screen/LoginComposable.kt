package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.ui.viewmodel.MainViewModel

@Preview
@Composable
fun LoginComposable(
    viewModel: MainViewModel = MainViewModel()
) {
    val login = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    Box {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = login.value,
                onValueChange = { value -> login.value = value },
                label = {
                    Text(text = stringResource(id = R.string.login_field_login))
                }
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { value -> password.value = value },
                label = {
                    Text(text = stringResource(id = R.string.login_field_password))
                }
            )

            Button(
                onClick = {
                    viewModel.onLogin(
                        Credentials(login.value, password.value)
                    )
                    login.value = ""
                    password.value = ""
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = dimensionResource(id = R.dimen.padding_l)),
            ) {
                Text(text = stringResource(id = R.string.login_button_login))
            }
        }
    }
}