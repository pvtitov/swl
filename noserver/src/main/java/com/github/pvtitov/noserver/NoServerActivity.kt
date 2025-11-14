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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(): $this")
        handleStartingIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent()")
        handleStartingIntent()
    }

    private fun handleStartingIntent() {
        coroutineScope.launch(Dispatchers.IO) {
            val extras = intent.extras ?: run {
                finish()
                return@launch
            }
            Log.d(TAG, "launchAuthorization(): intent = $intent, extras = ${extras.keySet()}")
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
            } else if (extras.containsKey(LOGOUT_EXTRA_KEY)) {
                GoogleDriveAuthorizationManager.logout(this@NoServerActivity)
            } else {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        GoogleDriveAuthorizer.onActivityResult(requestCode, resultCode, data)
        GoogleDriveAuthenticator.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AUTHORIZATION_REQUEST_CODE) {
            GoogleDriveAuthorizationManager.authorize()
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
        internal const val LOGOUT_EXTRA_KEY = "LOGOUT_EXTRA_KEY"
        internal const val FINISH_EXTRA_KEY = "FINISH_EXTRA_KEY"
        private const val AUTHORIZATION_REQUEST_CODE = 14
        private const val TAG = "NoServerActivity"
    }
}