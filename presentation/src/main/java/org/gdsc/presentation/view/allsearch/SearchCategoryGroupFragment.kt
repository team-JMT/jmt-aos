package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import org.gdsc.presentation.databinding.FragmentSearchCategoryGroupBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryGroupAdapter
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryGroupPreviewAdapter

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

        repeatWhenUiStarted {
            viewModel.searchedGroupPreviewState.collect {
                if (it.isEmpty()) {
                    binding.groupRecyclerView.visibility = View.GONE
                    binding.warningNoGroup.root.visibility = View.VISIBLE
                } else {
                    binding.groupRecyclerView.visibility = View.VISIBLE
                    binding.warningNoGroup.root.visibility = View.GONE

                }
            }
        }

        val adapter = SearchCategoryGroupAdapter()
        binding.groupRecyclerView.adapter = adapter
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        repeatWhenUiStarted {
            viewModel.searchedGroupState.collect {
                adapter.submitData(it)
            }
        }
    }
}