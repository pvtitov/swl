package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.model.User
import com.github.pvtitov.simplewishlist.domain.model.UserData
import com.github.pvtitov.simplewishlist.domain.model.Wish
import org.junit.Assert
import org.junit.Test

class JsonParserTest {

    private val jsonParser = DI.jsonParser

    @Test
    fun toJson() {
        val accountJson = jsonParser.toJson(DTO)
        Assert.assertEquals(JSON.trimIndentsAndSpaces(), accountJson?.trimIndentsAndSpaces())
    }

    @Test
    fun fromJson() {
        val accountData = jsonParser.fromJson(JSON, Dto::class.java)
        Assert.assertEquals(DTO, accountData)
    }

    private fun String.trimIndentsAndSpaces() {
        trimIndent()
            .replace("[\t\n ]".toRegex(), "")
    }
}

private val DTO = Dto(
    data = listOf(
        UserData(
            user = User(
                login = "test_login",
                name = "Test name",
            ),
            wishList = listOf(
                Wish(
                    title = "Spiderman",
                    description = "toy",
                ),
                Wish(
                    title = "Jenga",
                    description = "game",
                )
            ),
            promises = emptyMap(),
            date = 0L,
            checkSum = 0
        )
    ),
    sender = User(
        login = "test_login",
        name = "Test name",
    )
)

private val JSON = """
    {
    	"data": [
    		{
    			"user": {
    				"login": "test_login",
    				"name": "Test name"
    			},
    			"wishList": [
    				{
    					"title": "Spiderman",
    					"description": "toy"
    				},
    				{
    					"title": "Jenga",
    					"description": "game"
    				}
    			],
                "promises": [],
    			"date": 0,
    			"checkSum": 0
    		}
    	],
    	"sender": {
    		"login": "test_login",
    		"name": "Test name"
    	}
    }
"""