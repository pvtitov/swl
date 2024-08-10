package com.github.pvtitov.simplewishlist.ui.viewmodel

import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import com.github.pvtitov.simplewishlist.domain.model.Wish

class TestRepository : Repository {
    override suspend fun import() = Dto(
        data = listOf(
            UserData(
                user = User(
                    login = "user_1_login",
                    name = "user_1_name",
                ),
                wishList = listOf(
                    Wish("wish_1_1"),
                    Wish("wish_1_2"),
                    Wish("wish_1_3"),
                ),
                promises = emptyMap(),
                date = 0L,
                checkSum = 0,
            ),
            UserData(
                user = User(
                    login = "user_2_login",
                    name = "user_2_name",
                ),
                wishList = listOf(
                    Wish("wish_2_1"),
                    Wish("wish_2_2"),
                    Wish("wish_2_3"),
                ),
                promises = emptyMap(),
                date = 0L,
                checkSum = 0,
            ),
            UserData(
                user = User(
                    login = "user_3_login",
                    name = "user_3_name",
                ),
                wishList = listOf(
                    Wish("wish_3_1"),
                    Wish("wish_3_2"),
                    Wish("wish_3_3"),
                ),
                promises = emptyMap(),
                date = 0L,
                checkSum = 0,
            )
        ),
        sender = User(
            login = "sender_login",
            name = "sender_name",
        )
    )

    override suspend fun export(data: Dto): Boolean {
        TODO("Not yet implemented")
    }
}