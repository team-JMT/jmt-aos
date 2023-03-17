package org.gdsc.jmt.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.gdsc.jmt.model.LoginDataSourceImpl
import org.gdsc.jmt.view.GoogleLoginManager
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val loginDataSource = LoginDataSourceImpl()
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
                loginDataSource.postGoogleToken(token)
            }
            deferred.await()
        }
    }

    fun postAppleToken(email:String, clientId:String) {
        viewModelScope.launch {
            val deferred = async {
                loginDataSource.postAppleToken(email, clientId)
            }
            deferred.await()
        }
    }
}