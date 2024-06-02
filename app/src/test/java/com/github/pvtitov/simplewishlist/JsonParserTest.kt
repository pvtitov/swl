package com.github.pvtitov.simplewishlist

import com.github.pvtitov.simplewishlist.domain.data.model.AccountData
import com.github.pvtitov.simplewishlist.domain.model.Wish
import com.github.pvtitov.simplewishlist.utils.DI
import org.junit.Test

import org.junit.Assert.*

class JsonParserTest {

    private val jsonParser = DI.jsonParser

    @Test
    fun toJson() {
        val accountJson = jsonParser.toJson(ACCOUNT_DATA)
        assertEquals(ACCOUNT_JSON, accountJson)
    }

    @Test
    fun fromJson() {
        val accountData = jsonParser.fromJson(ACCOUNT_JSON, AccountData::class.java)
        assertEquals(ACCOUNT_DATA, accountData)
    }
}

private val ACCOUNT_DATA = AccountData(
    login = "",
    name = "Jack",
    wishes = listOf(
        Wish(
            title = "Spiderman",
            description = "toy",
            wishUrl = "http://google.com"
        ),
        Wish(
            title = "Jenga",
            description = "game",
            wishUrl = "http://yandex.ru"
        )
    )
)

private val ACCOUNT_JSON = """
    {
        "login": "",
        "name": "Jack",
        "wishes": [
            {
                "title": "Spiderman",
                "description": "toy",
                "wishUrl": "http://google.com"
            },
            {
                "title": "Jenga",
                "description": "game",
                "wishUrl": "http://yandex.ru"
            }
        ],
        "promises": {},
        "friends": []
    }
"""
    .replace("[\t\n ]".toRegex(), "")