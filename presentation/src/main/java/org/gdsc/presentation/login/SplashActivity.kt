package org.gdsc.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.WindowCompat
import org.gdsc.presentation.R

class SplashActivity : AppCompatActivity() {

    val handle: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setToFullPage()

        handle.postDelayed({
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }

            finish()
        }, 2000)
    }

    private fun setToFullPage() {
        val window = window
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}