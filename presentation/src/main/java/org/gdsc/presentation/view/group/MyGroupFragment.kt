package org.gdsc.presentation.view.group

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.databinding.FragmentMyGroupBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.WEB_BASE_URL
import org.gdsc.presentation.view.WebAppInterface
import org.gdsc.presentation.view.webview.SpecificWebViewViewModel

@AndroidEntryPoint
class MyGroupFragment: Fragment() {
    private var _binding: FragmentMyGroupBinding? = null
    private val binding get() = _binding!!

    private val specificWebViewViewModel: SpecificWebViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWebViewBackPress()
        
        binding.webView.apply {
            repeatWhenUiStarted {
                loadUrl(
                    WEB_BASE_URL,
                    hashMapOf<String, String>().apply {
                    put("token", specificWebViewViewModel.getAccessToken())
                })
            }

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()

            addJavascriptInterface(WebAppInterface(
                requireContext()
            ), "webviewBridge")

        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setWebViewBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                requireActivity().finish()
            }
        }
    }

}