package org.gdsc.presentation.view.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.presentation.view.mypage.adapter.RegisteredRestaurantPagingDataAdapter
import org.gdsc.presentation.databinding.FragmentRegisteredRestaurantBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class RegisteredRestaurantFragment : Fragment() {

    private var _binding: FragmentRegisteredRestaurantBinding? = null
    private val binding get() = _binding!!
    private lateinit var myRestaurantAdapter: RegisteredRestaurantPagingDataAdapter

    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisteredRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        setSpinners()

        myRestaurantAdapter = RegisteredRestaurantPagingDataAdapter()
        binding.registeredRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.registeredRestaurantRecyclerView.adapter = myRestaurantAdapter

        myRestaurantAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.append.endOfPaginationReached) {
                binding.notYetLayout.root.isVisible = myRestaurantAdapter.itemCount == 0
            } else {
                binding.notYetLayout.root.isVisible = false
            }

            if(combinedLoadStates.refresh is LoadState.Error) {
                binding.notYetLayout.root.isVisible = true
            }
        }

    }

    private fun observeState() {

        repeatWhenUiStarted {
            viewModel.idState.collectLatest { id ->

                id?.let { notNullId ->
                    viewModel.registeredPagingData(notNullId).collectLatest {
                        myRestaurantAdapter.submitData(it.data)
                    }
                }
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