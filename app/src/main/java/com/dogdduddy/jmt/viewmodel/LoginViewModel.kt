package com.dogdduddy.jmt.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogdduddy.jmt.view.GoogleLoginManager
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val googleLoginManager = GoogleLoginManager()

    suspend fun googleLogin(activity: Activity):PendingIntent {
        return googleLoginManager.signInIntent(activity)
    }

    fun getCredential(intent: Intent) :SignInCredential {
        return googleLoginManager.oneTapClient.getSignInCredentialFromIntent(intent)
    }
}