package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.noserver.NoServer
import com.github.pvtitov.simplewishlist.BuildConfig
import com.github.pvtitov.simplewishlist.data.datasource.LocalDataSourceImpl
import com.github.pvtitov.simplewishlist.data.datasource.RemoteDataSourceDefault
import com.github.pvtitov.simplewishlist.data.repository.LocalRepository
import com.github.pvtitov.simplewishlist.data.repository.RemoteRepositoryDefault
import com.github.pvtitov.simplewishlist.domain.interactor.Interactor
import com.github.pvtitov.simplewishlist.domain.interactor.InteractorDefault
import com.github.pvtitov.simplewishlist.utils.logger.Logger
import com.github.pvtitov.simplewishlist.utils.logger.LoggerDefault

object DI {

    val interactor: Interactor by lazy {
        InteractorDefault(
            remoteRepository = RemoteRepositoryDefault(RemoteDataSourceDefault()),
            localRepository = LocalRepository(LocalDataSourceImpl())
        )
    }

    val logger: Logger by lazy {
        LoggerDefault
    }

    fun init() {
        NoServer.configure(BuildConfig.GOOGLE_WEB_CLIENT_ID, BuildConfig.GOOGLE_API_KEY)
    }
}