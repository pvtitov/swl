package com.github.pvtitov.simplewishlist.data.repository

import com.github.pvtitov.simplewishlist.domain.model.Status
import kotlinx.coroutines.CancellationException

internal inline fun <R> runCatchingCancellable(block: () -> R): Status<R> {
    return try {
        Status.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Status.Failure(e)
    }
}