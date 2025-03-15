package com.github.pvtitov.simplewishlist.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.simplewishlist.data.WishListManualRepository
import com.github.pvtitov.simplewishlist.domain.data.Repository
import com.github.pvtitov.simplewishlist.domain.model.WishList

object DI {
    // Call before activity is created.
    // That is so because of using manual data source and activity result api requirements.
    fun getWishListRepository(activity: ComponentActivity): Repository<WishList> {
        check(activity.lifecycle.currentState == Lifecycle.State.INITIALIZED)
        return WishListManualRepository(activity)
    }

    private lateinit var activity: ComponentActivity

    fun prepareManualRepository(activity: ComponentActivity) {

    }

    val jsonParser: JsonParser by lazy { JsonParser() }

//    val wishListRepository
}