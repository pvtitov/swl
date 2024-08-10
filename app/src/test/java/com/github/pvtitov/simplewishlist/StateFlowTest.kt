package com.github.pvtitov.simplewishlist

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class StateFlowTest {

    @Test
    fun `WHEN emited 2 equal objects THEN collected 1`() = runTest {
        val flow = MutableStateFlow(State(false))
        val job = launch {
            assertEquals(State(true), flow.first())
        }
        flow.emit(State(false))
        flow.emit(State(true))
        job.join()
    }

    @Test
    fun `WHEN collected 2 times THEN receive value on every collection`() = runTest {
        val flow = MutableStateFlow(0)
        var counter = 0
        flow.emit(5)
        val job1 = launch {
            assertEquals(1, flow.first())
            counter++
        }
        val job2 = launch {
            assertEquals(1, flow.first())
            counter++
        }
        flow.emit(1)
        job1.join()
        job2.join()
        assertEquals(2, counter)
    }
}

private data class State(val value: Boolean)