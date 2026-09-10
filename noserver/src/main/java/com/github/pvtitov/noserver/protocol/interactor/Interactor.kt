package com.github.pvtitov.noserver.protocol.interactor

import com.github.pvtitov.noserver.protocol.repository.GoogleRepository
import com.github.pvtitov.noserver.protocol.repository.ManualRepository

class Interactor(dataType: Class<*>) {
    private val googleRepository by lazy { GoogleRepository() }
    private val manualRepository by lazy { ManualRepository() }
}