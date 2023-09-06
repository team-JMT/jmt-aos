package org.gdsc.presentation.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.databinding.FragmentHomeBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.WEB_BASE_URL
import org.gdsc.presentation.view.WebAppInterface

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = requireActivity() as MainActivity
        setWebViewBackPress()

        binding.webView.apply {
            loadUrl(WEB_BASE_URL)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()

            addJavascriptInterface(WebAppInterface(
                requireContext(),
                {
                    parentActivity.slideUpBottomNavigationView()
                },
                {
                    parentActivity.slideDownBottomNavigationView()
                },
                {
                    parentActivity.navigateToEditRestaurantInfo(it)
                },
                {
                    repeatWhenUiStarted {
                        binding.webView.loadUrl(
                            "javascript:setAccessToken(\"${viewModel.getAccessToken()}\")"
                        )
                    }
                }
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