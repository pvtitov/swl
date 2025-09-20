package com.github.pvtitov.noserver

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes

class AuthenticationManager {

    private var drive: Drive? = null

    suspend fun authenticate(context: Context): Result<Drive> {
        val driveImmutable = drive?.let { return Result.success(it) }
            ?: authenticateInternal(context, true)
            ?: authenticateInternal(context, false)

        return if (driveImmutable != null) {
            drive = driveImmutable
            Result.success(driveImmutable)
        } else {
            Result.failure(AuthenticationFailedException())
        }
    }

    private suspend fun authenticateInternal(
        context: Context,
        isAutoSelect: Boolean,
    ): Drive? {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(isAutoSelect)
            .build()

        val getCredentialRequest: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return handleSignIn(context, getCredentialRequest)
    }

    private suspend fun handleSignIn(context: Context, getCredentialRequest: GetCredentialRequest): Drive? {
        try {
            val credentialManager = CredentialManager.create(context)

            val getCredentialResponse = credentialManager.getCredential(
                context = context,
                request = getCredentialRequest,
            )

            return getCredentialResponse.credential.signIn(context)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Get credentials exception", e)
            return null
        } catch (e: Throwable) {
            Log.e(TAG, "Get credentials another exception", e)
            return if (e is UserRecoverableAuthIOException) {
                suspendForAuthorizationConsent(context, e.intent)
                handleSignIn(context, getCredentialRequest)
            } else {
                null
            }
        }
    }

    private suspend fun suspendForAuthorizationConsent(context: Context, intent: Intent) {
        // suspend coroutine to get consent
        (context as? Activity)?.startActivityForResult(intent, 55)
    }

    private fun Credential.signIn(context: Context): Drive? {

        if (this is CustomCredential && type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(data)

                if (drive == null) {
                    val googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                        context, listOf(DriveScopes.DRIVE)
                    )
                        .setSelectedAccount(
                            Account(googleIdTokenCredential.id, googleIdTokenCredential.type)
                        )

                    drive = Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        GsonFactory.getDefaultInstance(),
                        googleAccountCredential
                    )
                        .setApplicationName("Test")
                        .build()
                }

                return drive
            } catch (e: GoogleIdTokenParsingException) {
                Log.e(TAG, "Received an invalid google id token response", e)
                return null
            }
        } else {
            Log.e(TAG, "Unexpected type of credential")
            return null
        }
    }

    class AuthenticationFailedException(
        override val message: String? = null,
        override val cause: Throwable? = null
    ) : Exception(message, cause)
}

private const val TAG = "AuthenticationManager"