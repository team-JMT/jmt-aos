package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.base.CancelViewListener
import org.gdsc.presentation.base.SearchViewListener
import org.gdsc.presentation.databinding.FragmentAllSearchBinding

@AndroidEntryPoint
class AllSearchFragment: Fragment() {

    private var _binding: FragmentAllSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setSearchViewListener(searchListener)
        binding.searchBar.setCancelViewListener(cancelViewListener)
    }

    private val searchListener = object : SearchViewListener {
        override fun onSearchClear() {}
        override fun changeFocus(focus: Boolean) {}
        override fun onChangeText(text: CharSequence) {}
        override fun onSubmitText(text: CharSequence) {
            if (text.isEmpty()) return
            val action = AllSearchFragmentDirections.actionAllSearchFragmentToAllSearchContainerFragment(text.toString())
            findNavController().navigate(action)
        }
    }

    private val cancelViewListener = object : CancelViewListener {
        override fun onCancel() {
            findNavController().navigateUp()
        }
    }
}