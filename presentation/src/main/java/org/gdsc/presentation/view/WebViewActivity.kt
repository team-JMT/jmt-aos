package org.gdsc.presentation.view

import android.annotation.TargetApi
import android.content.pm.ActivityInfo
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.PermissionRequest
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.ActivityWebViewBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.webview.SpecificWebViewViewModel

@AndroidEntryPoint
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView: WebView

    private val specificWebViewViewModel: SpecificWebViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webView

        webViewInit()


        setWebViewBackPress()

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

    private fun webViewInit() {

        webView.apply {
            loadUrl(WEB_BASE_URL + "group-create/name/")

            settings.javaScriptEnabled = true // allow the js
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            addJavascriptInterface(WebAppInterface(this@WebViewActivity,
                setAccessToken = {
                    lifecycleScope.launch {
                        binding.webView.loadUrl(
                            "javascript:setAccessToken('${specificWebViewViewModel.getAccessToken()}')"
                        )
                    }
                }), "webviewBridge")
        }


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //화면이 계속 켜짐
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

    }
}
