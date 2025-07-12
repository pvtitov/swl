package com.github.pvtitov.noserver

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
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

object AuthenticationManager {
    private const val TAG = "AuthenticationManager"

    var drive: Drive? = null
        private set

    suspend fun authenticate(context: Context, isAutoSelect: Boolean = false) {
        Log.d(TAG, "authenticate() called")
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setAutoSelectEnabled(isAutoSelect)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            Log.d(TAG, "Credential manager = $credentialManager")
            val response = credentialManager.getCredential(
                context = context,
                request = request,
            )
            Log.d(TAG, "Got credentials: $response")
            handleSignIn(context, response).let { drive ->
                val filesList = drive?.files()?.list()?.setSpaces("drive")?.execute()?.files?.map { it.name }
                Log.d(TAG, "Drive files: $filesList")
            }
//            return handleSignIn(context, response)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Get credentials exception", e)
//            return null
        } catch (e: Throwable) {
            Log.e(TAG, "Get credentials another exception", e)
//            return null
            if (e is UserRecoverableAuthIOException) {
                (context as? Activity)?.startActivityForResult(e.intent, 55)
            }
        }
    }

    private fun handleSignIn(context: Context, response: GetCredentialResponse): Drive? {
        // Handle the successfully returned credential.
        val credential = response.credential

        // TODO do I need this? Should I return GoogleIdTokenCredential or null?
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate on your server.
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                if (drive == null) {
                    val googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                        context, listOf(DriveScopes.DRIVE)
                    )
                        .setSelectedAccount(
                            Account(googleIdTokenCredential.id, googleIdTokenCredential.type)
                        )// TODO do I need to set selectedAccount

                    Log.d(
                        TAG, """
                            googleAccountCredential = $googleAccountCredential
                            selected account = ${googleAccountCredential.selectedAccount}
                            selected account name = ${googleAccountCredential.selectedAccountName}
                            googleIdTokenCredential = $googleIdTokenCredential
                            googleIdTokenCredential id = ${googleIdTokenCredential.id}
                            googleIdTokenCredential idToken = ${googleIdTokenCredential.idToken}
                            googleIdTokenCredential type = ${googleIdTokenCredential.type}
                            googleIdTokenCredential givenName = ${googleIdTokenCredential.givenName}
                            googleIdTokenCredential displayName = ${googleIdTokenCredential.displayName}
                        """.trimIndent()
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
}