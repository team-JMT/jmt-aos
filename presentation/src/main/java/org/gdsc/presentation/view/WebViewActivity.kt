package org.gdsc.presentation.view

import android.annotation.TargetApi
import android.content.pm.ActivityInfo
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import org.gdsc.presentation.databinding.ActivityWebViewBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted


class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        webViewInit()

        webView = binding.webView

        setWebViewBackPress()
        intent.extras?.let {
            binding.webView.apply {

                repeatWhenUiStarted {
                    loadUrl(it.getString("url") ?: WEB_BASE_URL)
                }

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()

                addJavascriptInterface(WebAppInterface(context), "webviewBridge")
            }

        }

//        val actionBar: ActionBar? = supportActionBar
//        actionBar!!.hide()
    }
    private fun setWebViewBackPress() {
        this.onBackPressedDispatcher.addCallback(this) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
    }

    fun webViewInit() {
        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true // allow the js

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //화면이 계속 켜짐
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
        webView.webViewClient = WebViewClient()

        webView.addJavascriptInterface(WebAppInterface(this), "webviewBridge")
    }
}
