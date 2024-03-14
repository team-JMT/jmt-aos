package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.gdsc.domain.model.GroupInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSearchCategoryAllBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryGroupPreviewAdapter
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryRestaurantAdapter
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryRestaurantPreviewAdapter

@AndroidEntryPoint
class SearchCategoryAllFragment(
    private val searchKeyword: String
): Fragment() {

    private var _binding: FragmentSearchCategoryAllBinding? = null
    private val binding get() = _binding!!

    val viewModel: AllSearchViewModel by activityViewModels()

    private val searchCategoryRestaurantPreviewAdapter = SearchCategoryRestaurantPreviewAdapter()
    private val searchCategoryGroupPreviewAdapter = SearchCategoryGroupPreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCategoryAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.restaurantRecyclerView.adapter = searchCategoryRestaurantPreviewAdapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.groupRecyclerView.adapter = searchCategoryGroupPreviewAdapter
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Todo: New Adapter With Real APi
        binding.recommendedRestaurantRecyclerView.adapter = searchCategoryRestaurantPreviewAdapter
        binding.recommendedRestaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.icRestaurant.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.search_category_pager)
            viewPager.currentItem = 1
        }

        viewModel.setSearchKeyword(searchKeyword)
        observeState()
    }
    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.searchedRestaurantPreviewState.collect {
                searchCategoryRestaurantPreviewAdapter.submitList(it)
            }
        }

        // Todo: Real APi
        searchCategoryGroupPreviewAdapter.submitList(
            listOf(
                GroupInfo(
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    1,
                    "햄버거 먹으러 갈 사람 여기여기 모여라",
                    "버거 대마왕",
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    false,
                    (0 .. 500).shuffled().first(),
                    false,
                    (0 .. 500).shuffled().first()
                ),
                GroupInfo(
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    2,
                    "햄버거 먹으러 갈 사람 여기여기 모여라",
                    "버거 대마왕",
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    false,
                    (0 .. 500).shuffled().first(),
                    false,
                    (0 .. 500).shuffled().first()
                ),
                GroupInfo(
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    3,
                    "햄버거 먹으러 갈 사람 여기여기 모여라",
                    "버거 대마왕",
                    "https://avatars.githubusercontent.com/u/58663494?v=4",
                    false,
                    (0 .. 500).shuffled().first(),
                    false,
                    (0 .. 500).shuffled().first()
                ),
            )
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}