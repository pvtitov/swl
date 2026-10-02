package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.noserver.NoServer
import com.github.pvtitov.simplewishlist.BuildConfig
import com.github.pvtitov.simplewishlist.data.CompositeRepository

object DI {

    val compositeRepository: CompositeRepository by lazy { CompositeRepository() }

    fun init() {
        NoServer.configure(BuildConfig.GOOGLE_WEB_CLIENT_ID)
    }
}