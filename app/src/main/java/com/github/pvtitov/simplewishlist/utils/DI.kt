package com.github.pvtitov.simplewishlist.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.github.pvtitov.simplewishlist.data.GoogleDiskRepository
import com.github.pvtitov.simplewishlist.data.ManualRepository

object DI {

    lateinit var manualRepository: ManualRepository
        private set

    val googleDiskRepository: GoogleDiskRepository by lazy { GoogleDiskRepository() }

    val jsonParser: JsonParser by lazy { JsonParser() }

    // Call before activity is created.
    // That is so because of using manual data source and activity result api requirements.
    fun init(activity: ComponentActivity) {
        check(activity.lifecycle.currentState == Lifecycle.State.INITIALIZED)
        manualRepository = ManualRepository(activity)
    }
}