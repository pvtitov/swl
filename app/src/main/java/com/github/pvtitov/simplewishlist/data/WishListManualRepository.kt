package com.github.pvtitov.simplewishlist.data

import androidx.activity.ComponentActivity
import com.github.pvtitov.simplewishlist.domain.model.WishList
import com.github.pvtitov.simplewishlist.utils.DI

class WishListManualRepository(activity: ComponentActivity) :
    BaseManualRepository<WishList>(activity) {

    private val jsonParser = DI.jsonParser

    override fun serialize(data: WishList): String? {
        return jsonParser.toJson(data)
    }

    override fun deserialize(json: String): WishList? {
        return jsonParser.fromJson(json)
    }
}