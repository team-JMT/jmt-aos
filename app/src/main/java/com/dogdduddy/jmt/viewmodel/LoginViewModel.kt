package com.dogdduddy.jmt.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogdduddy.jmt.view.GoogleLoginManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val googleLoginManager = GoogleLoginManager()

    suspend fun googleLogin(activity: Activity):PendingIntent {
        Log.d("LoginTest", "googleLogin suspend method Start!!")
        return googleLoginManager.signInIntent(activity)
    }
}