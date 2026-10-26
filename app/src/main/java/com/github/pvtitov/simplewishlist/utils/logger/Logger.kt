package com.github.pvtitov.simplewishlist.utils.logger

interface Logger {
    fun debug(tag: String, message: String, error: Throwable? = null)
}