package com.github.pvtitov.simplewishlist.utils.logger

object LoggerTest : Logger {
    override fun debug(tag: String, message: String, error: Throwable?) {
        printOut(error, tag, message)
    }

    override fun error(tag: String, message: String, error: Throwable?) {
        printOut(error, tag, message)
    }

    private fun printOut(error: Throwable?, tag: String, message: String) {
        if (error != null) println("$tag: $message, $error") else println("$tag: $message")
    }


}