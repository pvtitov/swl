package com.github.pvtitov.noserver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.*


class NoServerActivity : Activity() {

    private lateinit var coroutineScope: CoroutineScope

    private var testAction: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        coroutineScope.launch(Dispatchers.IO) {
            val extras = intent.extras ?: return@launch
            if (extras.containsKey(AUTHORIZATION_EXTRA_KEY)) {
                Log.d(TAG, "Launch authorization")
                val isAuthorized = GoogleDriveAuthorizer.authorize(
                    activity = this@NoServerActivity,
                )
                if (isAuthorized) {
                    Log.d(TAG, "Success authorization")
                } else {
                    Log.d(TAG, "Failed authorization")
                }
            } else if (extras.containsKey(AUTHENTICATION_EXTRA_KEY)) {
                Log.d(TAG, "Launch authentication")
                val authenticationResult = GoogleDriveAuthenticator.authenticate(this@NoServerActivity)
                if (authenticationResult.isSuccess) {
                    Log.d(TAG, "On authentication result")
                    val drive = authenticationResult.getOrNull() ?: run {
                        Log.d(TAG, "Drive is null")
                        return@launch
                    }
                    try {
                        testAction = {
                            Log.d(TAG, "Successful authentication and authorization. Running an action $action")
                            action?.invoke(drive)
                        }
                        testAction?.invoke()
                    } catch (e: UserRecoverableAuthIOException) {
                        Log.d(TAG, "Failed to get drive file list: $e: ${e.message}, ${e.cause}", e)
                        e.intent?.let {
                            startActivityForResult(it, AUTHORIZATION_REQUEST_CODE)
                        }
                    } catch (e: Throwable) {
                        Log.e(TAG, "Failed to get drive file list: $e: ${e.message}, ${e.cause}", e)
                    }
                }
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