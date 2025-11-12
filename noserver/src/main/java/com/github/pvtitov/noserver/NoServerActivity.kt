package com.github.pvtitov.noserver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*


internal class NoServerActivity : Activity() {

    private val coroutineScope: CoroutineScope = CoroutineScope(
        Dispatchers.Default +
                SupervisorJob() +
                CoroutineExceptionHandler { _, e ->
                    Log.e(TAG, "Coroutine exception: $e, ${e.message}, ${e.cause}", e)
                }
    )

    private var testAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchAuthorization()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        launchAuthorization()
    }

    private fun launchAuthorization() {
        coroutineScope.launch(Dispatchers.IO) {
            val extras = intent.extras ?: run {
                finish()
                return@launch
            }
            if (extras.containsKey(AUTHORIZATION_EXTRA_KEY)) {
                val userRecoverableExceptionIntent = extras.getParcelable<Intent>(AUTHORIZATION_EXTRA_KEY)
                    ?: run {
                        finish()
                        return@launch
                    }

                Log.d(
                    TAG, "User recoverable exception intent: $userRecoverableExceptionIntent, " +
                            "calling activity = ${callingActivity}, calling package = ${callingPackage}, " +
                            "intent package = ${userRecoverableExceptionIntent.getPackage()}"
                )

                startActivityForResult(userRecoverableExceptionIntent, AUTHORIZATION_REQUEST_CODE)
            } else if (extras.containsKey(AUTHENTICATION_EXTRA_KEY)) {
                GoogleDriveAuthorizationManager.authenticate(this@NoServerActivity)
            } else {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        GoogleDriveAuthorizer.onActivityResult(requestCode, resultCode, data)
        GoogleDriveAuthenticator.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AUTHORIZATION_REQUEST_CODE -> {
                coroutineScope.launch(Dispatchers.IO) {
                    if (GoogleDriveAuthorizer.authorize(this@NoServerActivity)) {
                        testAction?.invoke()
                        testAction = null
                    } else {
                        Log.d(TAG, "Failed to authorize")
                    }
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
        internal const val AUTHENTICATION_EXTRA_KEY = "AUTHENTICATION_EXTRA_KEY"
        internal const val AUTHORIZATION_EXTRA_KEY = "AUTHORIZATION_EXTRA_KEY"
        private const val AUTHORIZATION_REQUEST_CODE = 14
        private const val TAG = "NoServerActivity"
    }
}