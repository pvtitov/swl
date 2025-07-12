package com.github.pvtitov.noserver

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.util.Log
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

object AuthorizationManager {
    private const val REQUEST_CODE = 11
    private const val TAG = "AuthorizationManager"

    private var onSuccessCallback: (() -> Unit)? = null
    private var onFailureCallback: (() -> Unit)? = null

    private var authorizationClient: AuthorizationClient? = null

    fun authorize(
        activity: Activity,
        authorizeRequestCode: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        onSuccessCallback = onSuccess
        onFailureCallback = onFailure

        val scopes = listOf(
            Scope(DriveScopes.DRIVE) // just DriveScopes.DRIVE in sample code https://developer.android.com/identity/authorization
        )

        val authorizationRequest = AuthorizationRequest.builder()
            .setRequestedScopes(scopes)
            .build()

        val client = Identity.getAuthorizationClient(activity)
        authorizationClient = client
        client
            .authorize(authorizationRequest)
            .addOnSuccessListener { authorizationResult ->
                if (authorizationResult.hasResolution()) {
                    // Access needs to be granted by the user
                    val pendingIntent: PendingIntent = authorizationResult.pendingIntent
                        ?: run {
                            Log.e(TAG, "Couldn't start Authorization UI")
                            onResult(isSuccess = false)
                            return@addOnSuccessListener
                        }
                    try {
                        startIntentSenderForResult(
                            activity,
                            pendingIntent.intentSender,
                            authorizeRequestCode,
                            null,
                            0,
                            0,
                            0,
                            null
                        )
                    } catch (e: SendIntentException) {
                        Log.e(TAG, "Couldn't start Authorization UI: " + e.localizedMessage)
                        onResult(isSuccess = false)
                    }
                } else {
                    // Access already granted, continue with user action
                    // Example: saveToDriveAppFolder(authorizationResult)
                    onResult(isSuccess = true)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to authorize", e)
                onResult(isSuccess = false)
            }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
                val authorizationResult = authorizationClient
                    ?.getAuthorizationResultFromIntent(data)

                if (resultCode == RESULT_OK && authorizationResult?.hasResolution() == true) {
                    onResult(isSuccess = true)
                } else {
                    onResult(isSuccess = false)
                }
            }
        }
    }

    private fun onResult(isSuccess: Boolean) {
        if (isSuccess) onSuccessCallback?.invoke() else onFailureCallback?.invoke()
        authorizationClient = null
    }
}