package com.github.pvtitov.simplewishlist.utils

import android.util.Log

object Logger {
    fun debug(tag: String, message: String, error: Throwable? = null) {
        Log.d(tag, message, error)
    }
}