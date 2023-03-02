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
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dogdduddy.jmt.R
import com.dogdduddy.jmt.databinding.ActivityLoginBinding
import com.dogdduddy.jmt.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val TAG = "LoginTest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                    Log.d(TAG, "Token : ${googleIdToken}")
                    loginViewModel.postToken(googleIdToken)
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