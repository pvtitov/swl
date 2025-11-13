package com.github.pvtitov.noserver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.pvtitov.noserver.NoServerActivity.Companion.AUTHENTICATION_EXTRA_KEY
import com.github.pvtitov.noserver.NoServerActivity.Companion.AUTHORIZATION_EXTRA_KEY
import com.github.pvtitov.noserver.NoServerActivity.Companion.LOGOUT_EXTRA_KEY
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.drive.Drive

object GoogleDriveAuthorizationManager {

    private const val TAG = "GoogleDriveAuthorizationManager"

    // TODO: implement action queue
    private var action: ((Drive) -> Unit)? = null

    fun runWithAuthorisation(action: (Drive) -> Unit) {
        this.action = action

        val driveImmutable = GoogleDriveAuthenticator.drive
        if (driveImmutable != null) {
            Log.d(TAG, "runWithAuthorisation(): run action (handle user recoverable exception)")
            handleUserRecoverableAuthException {
                action.invoke(driveImmutable)
            }
        } else {
            /**
             * Use of Google Drive starts with Authentication and Authorization checks. That API uses
             * Activity.onActivityResult(). For that I start stand-alone special Activity to contain (encapsulate) that
             * interaction. An alternative way to handle it would be to provide my main Activity but in that case it should
             * implement necessary
             */
            Log.d(TAG, "runWithAuthorisation(): launch NoServerActivity for authentication")
            NoServerApplication.applicationContext?.let { context ->
                context.startActivity(
                    Intent(context, NoServerActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra(AUTHENTICATION_EXTRA_KEY, true)
                    }
                )
            }
        }
    }

    fun logout() {
        NoServerApplication.applicationContext?.let { context ->
            context.startActivity(
                Intent(context, NoServerActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(LOGOUT_EXTRA_KEY, true)
                }
            )
        }
    }

    internal suspend fun logout(context: Context) {
        GoogleDriveAuthenticator.logout(context)
    }

    internal suspend fun authenticate(activity: Activity) {
        Log.d(TAG, "authenticate(Activity) called")
        val authenticationResult = GoogleDriveAuthenticator.authenticate(activity)
        if (authenticationResult.isSuccess) {
            Log.d(TAG, "On authentication result")
            val driveImmutable = authenticationResult.getOrNull() ?: run {
                Log.d(TAG, "Drive is null")
                return
            }
            val actionImmutable = action
            if (actionImmutable != null) {
                handleUserRecoverableAuthException {
                    actionImmutable.invoke(driveImmutable)
                }
            }
        }
    }

    internal fun authorize() {
        Log.d(TAG, "authorize() called")
        val actionImmutable = action
        val driveImmutable = GoogleDriveAuthenticator.drive
        if (actionImmutable != null && driveImmutable != null) {
            handleUserRecoverableAuthException {
                actionImmutable.invoke(driveImmutable)
            }
        }
    }

    private fun handleUserRecoverableAuthException(action: () -> Unit) {
        try {
            action.invoke()
            this.action = null
            Log.d(TAG, "handleUserRecoverableAuthException(): action succeeded")
        } catch (e: UserRecoverableAuthIOException) {
            Log.d(
                TAG,
                "handleUserRecoverableAuthException(): action failed with recoverable exception: $e, ${e.message}, ${e.cause}",
                e
            )
            val intent = e.intent
            val context = NoServerApplication.applicationContext
            if (context != null && intent != null) {
                context.startActivity(
                    Intent(context, NoServerActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        putExtra(AUTHORIZATION_EXTRA_KEY, intent)
                    }
                )
            }
        } catch (e: Throwable) {
            Log.e(TAG, "handleUserRecoverableAuthException(): action failed: $e, ${e.message}, ${e.cause}", e)
        }
    }
}