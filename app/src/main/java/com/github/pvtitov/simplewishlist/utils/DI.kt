package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.noserver.NoServer
import com.github.pvtitov.simplewishlist.BuildConfig
import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSourceImpl
import com.github.pvtitov.simplewishlist.data.datasource.RemoteDataSourceImpl
import com.github.pvtitov.simplewishlist.data.repository.LocalRepository
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepository
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.interactor.InteractorImpl
import com.github.pvtitov.simplewishlist.utils.logger.LoggerDefault

object DI {

    val interactor: Interactor by lazy {
        InteractorImpl(
            remoteRepository = RemoteRepository(RemoteDataSourceImpl(), LoggerDefault),
            localRepository = LocalRepository(LocalDataSourceImpl(), LoggerDefault),
            logger = LoggerDefault
        )
    }

    fun init() {
        NoServer.configure(BuildConfig.GOOGLE_WEB_CLIENT_ID, BuildConfig.GOOGLE_API_KEY)
    }
}