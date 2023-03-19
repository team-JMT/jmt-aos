package org.gdsc.presentation.view
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import org.gdsc.presentation.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = binding.webView
        webViewInit()
        intent.extras?.let {
            binding.webView.loadUrl(it.getString("url")!!)
        }

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()
    }
    fun webViewInit() {
        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true // allow the js

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //화면이 계속 켜짐
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER
        webView.setWebViewClient(WebViewClient())
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
    }
}