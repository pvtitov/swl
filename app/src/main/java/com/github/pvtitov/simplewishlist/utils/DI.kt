package com.github.pvtitov.simplewishlist.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.simplewishlist.data.ManualRepository
import com.github.pvtitov.simplewishlist.domain.data.Dto
import com.github.pvtitov.simplewishlist.domain.data.Repository

object DI {
    // Call before activity is created.
    // That is so because of using manual data source and activity result api requirements.
    fun getAccountDataSource(activity: ComponentActivity): Repository {
        check(activity.lifecycle.currentState == Lifecycle.State.INITIALIZED)
        return ManualRepository(activity)
    }

    val jsonParser: JsonParser<Dto> by lazy { JsonParser() }
}