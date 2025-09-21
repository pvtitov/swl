package com.github.pvtitov.noserver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*


class NoServerActivity : Activity() {

    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        coroutineScope.launch(Dispatchers.IO) {
            AuthenticationManager.authenticate(this@NoServerActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        AuthorizationManager.onActivityResult(requestCode, resultCode, data)
        AuthenticationManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
        super.onDestroy()
    }
}

private const val TAG = "NoServerActivity"