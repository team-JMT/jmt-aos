package com.dogdduddy.jmt.view

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.dogdduddy.jmt.R
import com.dogdduddy.jmt.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    // Google
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient: SignInClient
    private val TAG = "LoginTest"

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.my_web_client_id))
                .requestEmail()
                .requestProfile()
                .build())
    }
    // 구글 로그인 결과 받을 런처
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    toast(account.displayName.toString())
                    Log.d(TAG, "Access : ${account}")
                } catch (e: ApiException) {
                    Log.e(TAG, "Google Signin Exception : $e")
                }
            }
            else {
                Log.d(TAG, "result Not OK  result : ${result.resultCode}")
                Log.d(TAG, "result Not OK  result2 : ${result.data}")
                Log.d(TAG, "result Not OK  error : ${result}")
                Log.d(TAG, "result Not OK  : ${Activity.RESULT_OK}")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // 서버 클라이언트 ID (현재는 Firebase Web Client ID)
                    .setServerClientId(getString(R.string.my_web_client_id))
                    // 이전에 로그인하는 데 사용한 계정만 표시합니다.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

        binding.googleLoginBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            Log.d(TAG, "googleLogin() called $signInIntent")
            startForResult.launch(signInIntent)
        }

    }
    fun toast(string: String)  {
        Toast.makeText(this.applicationContext, string, Toast.LENGTH_LONG).show()
    }
}