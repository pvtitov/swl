package com.github.pvtitov.noserver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.*


class NoServerActivity : Activity() {

    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        openChooser()
        coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        coroutineScope.launch(Dispatchers.IO) {
            AuthenticationManager.authenticate(this@NoServerActivity, false)
        }
    }

    private fun openChooser() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, intent.getStringExtra(EXTRA_INPUT_DATA))
            type = JSON_MIME_TYPE
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivityForResult(shareIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        AuthorizationManager.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE -> {
                val isSuccess = resultCode == RESULT_OK

                finish()
            }
            55 -> {
                coroutineScope.launch(Dispatchers.IO) {
                    AuthenticationManager.authenticate(this@NoServerActivity, true)
                }
            }
        }
    }

    override fun onDestroy() {
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
        super.onDestroy()
    }

    companion object {
        const val EXTRA_INPUT_DATA = "EXTRA_INPUT_DATA"
        private const val REQUEST_CODE = 1000
        private const val JSON_MIME_TYPE = "text/json"
    }
}