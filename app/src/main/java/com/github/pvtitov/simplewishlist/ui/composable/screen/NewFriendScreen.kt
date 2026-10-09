package com.github.pvtitov.simplewishlist.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.github.pvtitov.simplewishlist.R

@Preview
@Composable
fun NewFriendScreen(
    onTopBarTitle: (String) -> Unit = {}
) {
    val (name, setName) = remember { mutableStateOf<String?>(null) }
    val (login, setLogin) = remember { mutableStateOf<String?>(null) }
    val paddingM = dimensionResource(id = R.dimen.padding_m)
    val nameField = stringResource(R.string.friend_field_name)
    val loginField = stringResource(R.string.friend_field_login)

    val topBarTitle = stringResource(R.string.new_friend_title)
    onTopBarTitle(topBarTitle) // TODO replace test implementation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingM),
        verticalArrangement = Arrangement.Center
    ) {
        val modifier = Modifier.fillMaxWidth()

        OutlinedTextField(
            value = name ?: "",
            onValueChange = {
                setName(it)
            },
            modifier = modifier,
            label = {
                Text(nameField)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        OutlinedTextField(
            value = login ?: "",
            onValueChange = {
                setLogin(it)
            },
            modifier = modifier,
            label = {
                Text(loginField)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
    }
}