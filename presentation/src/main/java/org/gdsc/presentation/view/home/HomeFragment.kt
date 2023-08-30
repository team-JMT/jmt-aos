package org.gdsc.presentation.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.databinding.FragmentHomeBinding
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.WebAppInterface

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        binding.webView.apply {
            loadUrl("https://jmt-matzip.dev")
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
                }
            ), "Android")
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}