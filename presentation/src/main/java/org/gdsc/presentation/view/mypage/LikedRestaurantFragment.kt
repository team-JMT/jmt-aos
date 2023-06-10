package org.gdsc.presentation.view.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.presentation.databinding.FragmentLikedRestaurantBinding
import org.gdsc.presentation.view.mypage.viewmodel.LikedRestaurantViewModel

@AndroidEntryPoint
class LikedRestaurantFragment : Fragment() {

    private var _binding: FragmentLikedRestaurantBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LikedRestaurantViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setSpinners()

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

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.sortTypeState.collectLatest {
                binding.sortSpinner.setMenuTitle(it.text)
            }
        }

        lifecycleScope.launch {
            viewModel.foodCategoryState.collectLatest {
                binding.foodCategorySpinner.setMenuTitle(it.text)
            }
        }

        lifecycleScope.launch {
            viewModel.drinkPossibilityState.collectLatest {
                binding.drinkPossibilitySpinner.setMenuTitle(it.text)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}