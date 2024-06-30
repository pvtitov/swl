package com.github.pvtitov.simplewishlist.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.pvtitov.simplewishlist.R
import com.github.pvtitov.simplewishlist.domain.model.Credentials
import com.github.pvtitov.simplewishlist.ui.viewmodels.LoginViewModel

@Composable
fun LoginComposable(
    loginViewModel: LoginViewModel,
    modifier: Modifier
) {
    val login = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = login.value,
            onValueChange = { value -> login.value = value },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = {
                Text(text = stringResource(id = R.string.field_login))
            }
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { value -> password.value = value },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = {
                Text(text = stringResource(id = R.string.field_password))
            }
        )

        Button(
            onClick = {
                loginViewModel.onLogin(
                    Credentials(login.value, password.value)
                )
                login.value = ""
                password.value = ""
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(text = stringResource(id = R.string.button_login))
        }
    }
}