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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.gdsc.domain.usecase.PostAppleTokenUseCase
import org.gdsc.domain.usecase.PostGoogleTokenUseCase
import org.gdsc.presentation.R
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val postGoogleTokenUseCase: PostGoogleTokenUseCase,
    private val postAppleTokenUseCase: PostAppleTokenUseCase
) : ViewModel() {
    private val loginManager = LoginManager()

    suspend fun googleLogin(activity: Activity): PendingIntent {
        return loginManager.signInIntent(activity)
    }

    fun getGoogleCredential(intent: Intent): SignInCredential {
        return loginManager.oneTapClient.getSignInCredentialFromIntent(intent)
    }

    fun postGoogleToken(token: String) {
        viewModelScope.launch {
            val response = postGoogleTokenUseCase.invoke(token)
        }
    }

    fun appleLogin(activity: Activity) {
        viewModelScope.launch {
            val provider = OAuthProvider.newBuilder(activity.getString(R.string.apple_provider))
            provider.scopes = mutableListOf("email", "name")

            val auth = loginManager.auth

            val pendingAuthResult = auth.pendingAuthResult?.await()

            if (pendingAuthResult != null) {

                val getToken = pendingAuthResult.user?.getIdToken(true)?.await()

                getToken?.let {
                    postAppleToken(
                        pendingAuthResult.user?.email.toString(),
                        activity.getString(R.string.ios_client_id)
                    )
                } ?: Log.w(TAG, "getIdToken:onFailure")

            } else {
                Log.w(TAG, "pending:onFailure")

                auth.startActivityForSignInWithProvider(activity, provider.build()).await()
                    .let { authResult ->
                        postAppleToken(
                            authResult.user?.email.toString(),
                            activity.getString(R.string.ios_client_id)
                        )
                    }
            }

        }

    }

    private fun postAppleToken(email: String, clientId: String) {
        viewModelScope.launch {
            val response = postAppleTokenUseCase.invoke(email, clientId)
            Log.d(TAG, "postAppleToken: $response")
        }
    }

    companion object {
        private const val TAG = "TestViewModel"
    }

}