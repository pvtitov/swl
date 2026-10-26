package com.github.pvtitov.simplewishlist.utils.logger

import android.util.Log

object LoggerDefault : Logger {
    override fun debug(tag: String, message: String, error: Throwable?) {
        Log.d(tag, message, error)
    }
}