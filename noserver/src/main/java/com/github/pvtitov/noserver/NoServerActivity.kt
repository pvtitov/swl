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
            val extras = intent.extras ?: return@launch
            val authorizationRequestCode = extras.getInt(AUTHORIZATION_EXTRA_KEY, NO_AUTHORIZATION_REQUEST_CODE)
            if (authorizationRequestCode != NO_AUTHORIZATION_REQUEST_CODE) {
                Log.d(TAG, "Launch authorization with request code $authorizationRequestCode")
                AuthorizationManager.authorize(
                    activity = this@NoServerActivity,
                    authorizeRequestCode = authorizationRequestCode,
                    onSuccess = {
                        Log.d(TAG, "Success authorization")
                    },
                    onFailure = {
                        Log.d(TAG, "Failed authorization")
                    }
                )
            } else if (extras.containsKey(AUTHENTICATION_EXTRA_KEY)) {
                Log.d(TAG, "Launch authentication")
                val authenticationResult = AuthenticationManager.authenticate(this@NoServerActivity)
                if (authenticationResult.isSuccess) {
                    Log.d(TAG, "On authentication result")
                    val drive = authenticationResult.getOrNull() ?: run {
                        Log.d(TAG, "Drive is null")
                        return@launch
                    }
                    // TODO replace testing use of Drive with actual implementation
                    Log.d(TAG, drive.files().list().setSpaces("drive").execute().files.map { it.name }.toString())
                }
            } else {
                finish()
            }
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

    companion object {
        const val AUTHENTICATION_EXTRA_KEY = "AUTHENTICATION_EXTRA_KEY"
        const val AUTHORIZATION_EXTRA_KEY = "AUTHORIZATION_EXTRA_KEY"
        const val NO_AUTHORIZATION_REQUEST_CODE = 0

        private const val TAG = "NoServerActivity"
    }
}