package com.github.pvtitov.simplewishlist.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.simplewishlist.data.source.ManualAccountDataSource
import com.github.pvtitov.simplewishlist.domain.data.source.AccountDataSource

object DI {
    // Call before activity is created.
    // That is so because of using manual data source and activity result api requirements.
    fun getAccountDataSource(activity: ComponentActivity): AccountDataSource {
        check(activity.lifecycle.currentState == Lifecycle.State.INITIALIZED)
        return ManualAccountDataSource(activity)
    }
}