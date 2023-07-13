package com.github.pvtitov.simplewishlist.domain.model

sealed interface Status<out T> {
    class Success<T>(val result: T) : Status<T>

    class Failure(val error: Throwable? = null) : Status<Nothing>
}