package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.gdsc.presentation.databinding.FragmentSearchCategoryGroupBinding

class SearchCategoryGroupFragment(
    private val searchKeyword: String
): Fragment() {

    private var _binding: FragmentSearchCategoryGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllSearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCategoryGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}