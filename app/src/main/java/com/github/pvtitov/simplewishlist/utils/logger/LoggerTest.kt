package com.github.pvtitov.simplewishlist.utils.logger

object LoggerTest : Logger {
    override fun debug(tag: String, message: String, error: Throwable?) {
        if (error != null) println("$tag: $message, $error") else println("$tag: $message")
    }
}