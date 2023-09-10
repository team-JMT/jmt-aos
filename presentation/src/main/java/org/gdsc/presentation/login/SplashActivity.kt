package org.gdsc.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.domain.usecase.token.GetRefreshTokenUseCase
import org.gdsc.domain.usecase.token.PostRefreshTokenUseCase
import org.gdsc.domain.usecase.token.VerifyAccessTokenUseCase
import org.gdsc.presentation.R
import org.gdsc.presentation.view.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    val handle: Handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var verifyAccessTokenUseCase: VerifyAccessTokenUseCase

    @Inject
    lateinit var postRefreshTokenUseCase: PostRefreshTokenUseCase

    @Inject
    lateinit var getRefreshTokenUseCase: GetRefreshTokenUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setToFullPage()

        handle.postDelayed({

            lifecycleScope.launch {
                val isAccessTokenValid = verifyAccessTokenUseCase.invoke()

                // 정상이면 그냥 리프레쉬
                if (isAccessTokenValid) {
                    postRefreshTokenUseCase.invoke(getRefreshTokenUseCase.invoke())
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                // access Token이 만료되었다면
                } else {
                    // refresh 시도
                    val isRefreshSuccess = postRefreshTokenUseCase.invoke(getRefreshTokenUseCase.invoke())

                    // refresh 성공했다면
                    if (isRefreshSuccess) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    // refresh 실패했다면
                    } else {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

        }, 2000)
    }

    private fun setToFullPage() {
        val window = window
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}