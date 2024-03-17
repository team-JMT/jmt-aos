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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.FragmentMyGroupBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.WEB_BASE_URL
import org.gdsc.presentation.view.WebAppInterface
import org.gdsc.presentation.view.webview.SpecificWebViewViewModel

@AndroidEntryPoint
class MyGroupFragment: Fragment() {
    private var _binding: FragmentMyGroupBinding? = null
    private val binding get() = _binding!!

    private val specificWebViewViewModel: SpecificWebViewViewModel by viewModels()

    private val parentActivity by lazy { requireActivity() as MainActivity }
    private val navArgs by navArgs<MyGroupFragmentArgs>()

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
                loadUrl(WEB_BASE_URL + navArgs.route)
            }

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()

            addJavascriptInterface(WebAppInterface(
                mContext = requireContext(),
                slideUpBottomNavigationView = {
                    parentActivity.slideUpBottomNavigationView()
                },
                slideDownBottomNavigationView ={
                    parentActivity.slideDownBottomNavigationView()
                },
                setAccessToken = {
                    viewLifecycleOwner.lifecycleScope.launch {
                        binding.webView.loadUrl(
                            "javascript:setAccessToken('${specificWebViewViewModel.getAccessToken()}')"
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