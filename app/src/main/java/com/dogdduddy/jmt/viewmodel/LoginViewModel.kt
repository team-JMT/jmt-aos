package com.dogdduddy.jmt.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogdduddy.jmt.model.Data
import com.dogdduddy.jmt.view.GoogleLoginManager
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val data = Data()
    private val googleLoginManager = GoogleLoginManager()

    suspend fun googleLogin(activity: Activity):PendingIntent {
        return googleLoginManager.signInIntent(activity)
    }

    fun getCredential(intent: Intent) :SignInCredential {
        return googleLoginManager.oneTapClient.getSignInCredentialFromIntent(intent)
    }
    fun postGoogleToken(token:String?) {
        viewModelScope.launch {
            val deferred = async {
                data.postGoogleToken(token)
            }
            deferred.await()
        }
    }

    fun postAppleToken(token:String?) {
        viewModelScope.launch {
            val deferred = async {
                data.postAppleToken(token)
            }
            deferred.await()
        }
    }

    fun testPost() {
        viewModelScope.launch {
            val deferred = async {
                data.testPost()
            }
            deferred.await()
        }
    }
}