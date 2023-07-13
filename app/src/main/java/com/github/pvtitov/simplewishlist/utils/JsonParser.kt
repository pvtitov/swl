package com.github.pvtitov.simplewishlist.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class JsonParser<T> {
    private val gson = Gson()

    fun fromJson(json: String, type: Class<T>): T? {
        return try {
            gson.fromJson(json, type)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    fun toJson(instance: T): String? {
        return gson.toJson(instance)
    }
}