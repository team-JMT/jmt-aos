package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSearchCategoryAllBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted

@AndroidEntryPoint
class SearchCategoryAllFragment(
    private val searchKeyword: String
): Fragment() {

    private var _binding: FragmentSearchCategoryAllBinding? = null
    private val binding get() = _binding!!


    val viewModel: AllSearchViewModel by viewModels()


    private val searchCategoryRestaurantAdapter = SearchCategoryRestaurantAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCategoryAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restaurantRecyclerView.adapter = searchCategoryRestaurantAdapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.icRestaurant.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.search_category_pager)
            viewPager.currentItem = 1
        }

        viewModel.setSearchKeyword(searchKeyword)
        observeState()
    }
    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.registeredPagingData().collect {
                searchCategoryRestaurantAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}