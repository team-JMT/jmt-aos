package org.gdsc.presentation.view.webview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.domain.Empty
import org.gdsc.presentation.databinding.FragmentSpecificWebViewBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.WebAppInterface

@AndroidEntryPoint
class SpecificWebViewFragment : Fragment() {

    private var _binding: FragmentSpecificWebViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SpecificWebViewViewModel by viewModels()

    private val navArgs by navArgs<SpecificWebViewFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentActivity = requireActivity() as MainActivity
        parentActivity.changeToolbarTitle(String.Empty)
        parentActivity.changeToolbarVisible(false)

        binding.webView.apply {
            loadUrl(navArgs.url)

            (requireActivity() as MainActivity).let {
                it.detailLink?.let { link ->
                    binding.webView.loadUrl(link)
                }
            }

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

}