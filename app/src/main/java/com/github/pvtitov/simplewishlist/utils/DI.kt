package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.noserver.NoServer
import com.github.pvtitov.simplewishlist.BuildConfig
import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSource
import com.github.pvtitov.simplewishlist.data.datasource.NoServerDataSource
import com.github.pvtitov.simplewishlist.data.repository.LocalRepositoryImpl
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepositoryImpl
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.interactor.InteractorImpl

object DI {

    val interactor: Interactor by lazy {
        InteractorImpl(
            remoteRepository = RemoteRepositoryImpl(NoServerDataSource()),
            localRepository = LocalRepositoryImpl(LocalDataSource())
        )
    }

    fun init() {
        NoServer.configure(BuildConfig.GOOGLE_WEB_CLIENT_ID, BuildConfig.GOOGLE_API_KEY)
    }
}