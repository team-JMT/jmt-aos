package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.presentation.databinding.FragmentSearchCategoryRestaurantBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryRestaurantAdapter

@AndroidEntryPoint
class SearchCategoryRestaurantFragment(
    private val searchKeyword: String
): Fragment() {

    private var _binding: FragmentSearchCategoryRestaurantBinding? = null
    private val binding get() = _binding!!

    val viewModel: AllSearchViewModel by activityViewModels()
    private val searchCategoryRestaurantAdapter = SearchCategoryRestaurantAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCategoryRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setSpinners()

        viewModel.setSearchKeyword(searchKeyword)
        viewModel.searchRestaurantWithKeyword()

        binding.restaurantRecyclerView.adapter = searchCategoryRestaurantAdapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun observeState() {

        repeatWhenUiStarted {
            viewModel.searchedRestaurantState.collect {
                searchCategoryRestaurantAdapter.submitData(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.sortTypeState.collectLatest {
                binding.sortSpinner.setMenuTitle(it.text)
            }
        }

        repeatWhenUiStarted {
            viewModel.foodCategoryState.collectLatest {
                binding.foodCategorySpinner.setMenuTitle(it.text)
            }
        }

        repeatWhenUiStarted  {
            viewModel.drinkPossibilityState.collectLatest {
                binding.drinkPossibilitySpinner.setMenuTitle(it.text)
            }
        }
    }

    private fun setSpinners() {
        binding.sortSpinner.setMenu(
            SortType.getAllText()
        ) {
            viewModel.setSortType(SortType.values()[it.itemId])
        }

        binding.foodCategorySpinner.setMenu(
            FoodCategory.getAllText()
        ) {
            viewModel.setFoodCategory(FoodCategory.values()[it.itemId])
        }

        binding.drinkPossibilitySpinner.setMenu(
            DrinkPossibility.getAllText()
        ) {
            viewModel.setDrinkPossibility(DrinkPossibility.values()[it.itemId])
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}