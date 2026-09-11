package com.github.pvtitov.simplewishlist.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.noserver.NoServer
import com.github.pvtitov.simplewishlist.BuildConfig
import com.github.pvtitov.simplewishlist.data.CompositeRepository
import com.github.pvtitov.simplewishlist.data.ManualRepository

object DI {

    lateinit var manualRepository: ManualRepository
        private set

    val compositeRepository: CompositeRepository by lazy { CompositeRepository() }

    val jsonParser: JsonParser by lazy { JsonParser() }

    // Call before activity is created.
    // That is so because of using manual data source and activity result api requirements.
    fun init(activity: ComponentActivity) {
        check(activity.lifecycle.currentState == Lifecycle.State.INITIALIZED)
        manualRepository = ManualRepository(activity)
        NoServer.configure(BuildConfig.GOOGLE_WEB_CLIENT_ID)
    }
}