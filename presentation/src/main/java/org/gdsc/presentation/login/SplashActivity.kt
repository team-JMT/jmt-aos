package org.gdsc.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gdsc.domain.usecase.token.GetRefreshTokenUseCase
import org.gdsc.domain.usecase.token.PostRefreshTokenUseCase
import org.gdsc.domain.usecase.token.VerifyAccessTokenUseCase
import org.gdsc.presentation.R
import org.gdsc.presentation.view.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var verifyAccessTokenUseCase: VerifyAccessTokenUseCase

    @Inject
    lateinit var postRefreshTokenUseCase: PostRefreshTokenUseCase

    @Inject
    lateinit var getRefreshTokenUseCase: GetRefreshTokenUseCase

    private val DELAY_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setToFullPage()

        lifecycleScope.launch(Dispatchers.Main) {

            val accessible = validateToken()

            delay(DELAY_TIME)

            if(accessible)
                moveToMain()
            else
                moveToLogin()
        }
    }

    private fun moveToMain() {
        startActivity(Intent(applicationContext,MainActivity::class.java))
        finish()
    }

    private fun moveToLogin() {
        startActivity(Intent(applicationContext,LoginActivity::class.java))
        finish()
    }

    private suspend fun validateToken(): Boolean {
        val isAccessTokenValid = verifyAccessTokenUseCase.invoke()


        return if (isAccessTokenValid) { // 정상이면 그냥 리프레쉬
            postRefreshTokenUseCase.invoke(getRefreshTokenUseCase.invoke())
            true
        } else {
            // refresh 시도
            val isRefreshSuccess = postRefreshTokenUseCase.invoke(getRefreshTokenUseCase.invoke())
            isRefreshSuccess
        }
    }

    private fun setToFullPage() {
        val window = window
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}