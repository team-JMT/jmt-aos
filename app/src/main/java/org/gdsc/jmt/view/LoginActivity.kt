package org.gdsc.jmt.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import org.gdsc.jmt.R
import org.gdsc.jmt.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.gdsc.jmt.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel = LoginViewModel()
    private val TAG = "LoginTest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.appleLoginBtn.setOnClickListener {
            loginViewModel.appleLogin(this)
        }
        
        binding.googleLoginBtn.setOnClickListener {
            lifecycleScope.launch(CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }) {
                startForResult.launch(
                    IntentSenderRequest.Builder(loginViewModel.googleLogin(this@LoginActivity)).build()
                )
            }
        }
    }
    private val startForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult( ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val credential = loginViewModel.getGoogleCredential(intent)
                    val googleIdToken = credential.googleIdToken

                    loginViewModel.postGoogleToken(googleIdToken)
                } else {
                    Log.d(TAG, "Google Login Failed")
                }
            } else {
                Log.e(TAG, "Result : ${result.resultCode}")
            }
        }

}