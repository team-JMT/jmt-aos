package org.gdsc.presentation.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import org.gdsc.presentation.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.ActivityTestBinding

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding

    private val viewModel: TestViewModel by viewModels()

    private val TAG = "LoginTest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appleLoginBtn.setOnClickListener {
            viewModel.appleLogin(this)
        }

        binding.googleLoginBtn.setOnClickListener {
            lifecycleScope.launch(CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }) {
                startForResult.launch(
                    IntentSenderRequest.Builder(viewModel.googleLogin(this@TestActivity)).build()
                )
            }
        }
    }
    private val startForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult( ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val credential = viewModel.getGoogleCredential(intent)
                    val googleIdToken = credential.googleIdToken

                    Log.d(TAG, "Google Login Success : $googleIdToken")

                    // TODO : 토큰이 null일 경우 예외 처리
                    googleIdToken?.let {
                        viewModel.postGoogleToken(it)
                    }

                } else {
                    Log.d(TAG, "Google Login Failed")
                }
            } else {
                Log.e(TAG, "Result : ${result.resultCode}")
            }
        }

}