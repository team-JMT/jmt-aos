package com.dogdduddy.jmt.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dogdduddy.jmt.R
import com.dogdduddy.jmt.databinding.ActivityLoginBinding
import com.dogdduddy.jmt.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val TAG = "LoginTest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// 애플 로그인
        binding.appleLoginBtn.setOnClickListener {
            Log.d(TAG, "appleLoginBtn Clicked")
            val provider = OAuthProvider.newBuilder("apple.com")
            provider.setScopes(mutableListOf("email", "name"))

            val auth = FirebaseAuth.getInstance()

            val pending = auth.pendingAuthResult
            if (pending != null) {
                Log.d(TAG, "peding : not null")
                pending.addOnSuccessListener { authResult ->
                    Log.d(TAG, "checkPending:onSuccess:$authResult")
                    // Get the user profile with authResult.getUser() and
                    // authResult.getAdditionalUserInfo(), and the ID
                    // token from Apple with authResult.getCredential().

                    authResult.user?.getIdToken(true)?.addOnSuccessListener {
                        Log.d(TAG, "checkPending:onSuccess:Token  :  ${it.token}")
                    }
                }.addOnFailureListener { e ->
                    Log.w(TAG, "checkPending:onFailure", e)
                }
            }

            // 대기 중 결과가 없다면 실행
            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener { authResult ->
                    loginViewModel.postAppleToken(authResult.user?.email.toString(), getString(R.string.ios_client_id))
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "activitySignIn:onFailure", e)
                }
        }

        ///

        binding.googleLoginBtn.setOnClickListener {
            lifecycleScope.launch(CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }) {
                startForResult.launch(
                    IntentSenderRequest.Builder(loginViewModel.googleLogin(this@LoginActivity)).build()
                )
            }
        }
        binding.testBtn.setOnClickListener {
            loginViewModel.testPost()
        }
    }
    private val startForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult( ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val credential = loginViewModel.getCredential(intent)
                    val googleIdToken = credential.googleIdToken

                    loginViewModel.postGoogleToken(googleIdToken)
                } else {
                    Log.d(TAG, "Google Login Failed")
                }
            } else {
                Log.e(TAG, "Result : ${result.resultCode}")
            }
        }
    fun toast(string: String)  {
        Toast.makeText(this.applicationContext, string, Toast.LENGTH_LONG).show()
    }
}