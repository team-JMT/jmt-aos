package org.gdsc.presentation.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gdsc.domain.usecase.token.GetRefreshTokenUseCase
import org.gdsc.domain.usecase.token.PostRefreshTokenUseCase
import org.gdsc.domain.usecase.token.VerifyAccessTokenUseCase
import org.gdsc.presentation.R
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.JmtAlert
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

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        if (permissions.all { it.value }) {
            Toast.makeText(this, "위치 권한이 허용되었습니다!", Toast.LENGTH_SHORT).show()
        } else {
            showLocationPermissionDialog()
        }

    }

    private fun checkLocationRequest() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun showLocationPermissionDialog() {
        JmtAlert(this)
            .title("위치 권한 요청")
            .content("위치 권한 설정 창으로 이동할까요?")
            .multiButton {
                leftButton("네") {

                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null)
                        )
                    )
                }
                rightButton("아니오") {
                    Toast.makeText(this@SplashActivity, "위치 권한이 꼭 필요합니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setToFullPage()

        lifecycleScope.launch(Dispatchers.Main) {

            val accessible = validateToken()

            delay(DELAY_TIME)

            checkLocationRequest()
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