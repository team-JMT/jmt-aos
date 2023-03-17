package org.gdsc.jmt.view

import android.app.Activity
import org.gdsc.jmt.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.suspendCoroutine
class LoginManager {
    private val auth = FirebaseAuth.getInstance()
    lateinit var oneTapClient: SignInClient
    suspend fun signInIntent(activity: Activity)  = suspendCoroutine { continuation ->
        oneTapClient = Identity.getSignInClient(activity)
        val signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(activity.getString(R.string.my_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnFailureListener { continuation.resumeWith(Result.failure(it)) }
            .addOnSuccessListener {
                continuation.resumeWith(Result.success(it.pendingIntent))
            }
    }
}