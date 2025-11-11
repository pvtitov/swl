package com.github.pvtitov.noserver

import android.content.Intent
import com.github.pvtitov.noserver.NoServerActivity.Companion.AUTHENTICATION_EXTRA_KEY
import com.google.api.services.drive.Drive

class GoogleDriveAuthorizationManager {

    private var drive: Drive? = null

    fun getDriveWithAuthorization(): Drive? {
        if (drive == null) {
            // todo get drive
        } else {
            drive
        }
    }

    // TODO: implement action queue
    private var action: ((Drive) -> Unit)? = null

    fun runWithAuthorisation(action: (Drive) -> Unit) {
        this.action = action

        /**
         * Use of Google Drive starts with Authentication and Authorization checks. That API uses
         * Activity.onActivityResult(). For that I start stand-alone special Activity to contain (encapsulate) that
         * interaction. An alternative way to handle it would be to provide my main Activity but in that case it should
         * implement necessary
         */
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