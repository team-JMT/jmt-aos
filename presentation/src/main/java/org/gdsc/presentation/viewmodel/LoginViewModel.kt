package org.gdsc.presentation.viewmodel

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.gdsc.presentation.view.LoginManager
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.gdsc.domain.usecase.PostAppleTokenUseCase
import org.gdsc.domain.usecase.PostGoogleTokenUseCase
import org.gdsc.presentation.R
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postGoogleTokenUseCase: PostGoogleTokenUseCase,
    private val postAppleTokenUseCase: PostAppleTokenUseCase
): ViewModel() {
    private val loginManager = LoginManager()

    suspend fun googleLogin(activity: Activity):PendingIntent {
        return loginManager.signInIntent(activity)
    }

    fun getGoogleCredential(intent: Intent) :SignInCredential {
        return loginManager.oneTapClient.getSignInCredentialFromIntent(intent)
    }

    fun postGoogleToken(token:String) {
        viewModelScope.launch {
            val deferred = async {
                postGoogleTokenUseCase.invoke(token)
            }
            deferred.await()
        }
    }

    fun appleLogin(activity:Activity) {
        val provider = OAuthProvider.newBuilder(activity.getString(R.string.apple_provider))
        provider.scopes = mutableListOf("email", "name")

        val auth = loginManager.auth
        val pending = auth.pendingAuthResult

        pending?.addOnSuccessListener { authResult ->
            authResult.user?.getIdToken(true)?.addOnSuccessListener {
                postAppleToken(authResult.user?.email.toString(), activity.getString(R.string.ios_client_id))
            }
            ?.addOnFailureListener { e ->
                Log.w(TAG, "getIdToken:onFailure", e)
            }
        }?.addOnFailureListener { e ->
            Log.w(TAG, "checkPending:onFailure", e)
        }

        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                postAppleToken(authResult.user?.email.toString(), activity.getString(R.string.ios_client_id))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "activitySignIn:onFailure", e)
            }
    }

    private fun postAppleToken(email:String, clientId:String) {
        viewModelScope.launch {
            val deferred = async {
                postAppleTokenUseCase.invoke(email, clientId)
            }
            deferred.await()
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

}