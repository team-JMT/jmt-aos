package org.gdsc.presentation.view.restaurantregistration

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.domain.Empty
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentRegisterRestaurantBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.utils.animateExtendWidth
import org.gdsc.presentation.utils.animateShrinkWidth
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.FoodCategoryBottomSheetDialog
import org.gdsc.presentation.view.restaurantregistration.adapter.RegisterRestaurantAdapter
import org.gdsc.presentation.view.restaurantregistration.adapter.RestaurantLocationInfoAdapter
import org.gdsc.presentation.view.restaurantregistration.viewmodel.RegisterRestaurantViewModel

@AndroidEntryPoint
class RegisterRestaurantFragment : Fragment() {

    private var _binding: FragmentRegisterRestaurantBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: RegisterRestaurantViewModel by viewModels()

    private val navArgs by navArgs<RegisterRestaurantFragmentArgs>()

    private val foodCategoryDialog by lazy {
        FoodCategoryBottomSheetDialog { selectedItem ->
            selectedItem?.let {
                viewModel.setFoodCategoryState(it)
            }
        }
    }

    private val adapter by lazy { RegisterRestaurantAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStates()
        setFoodCategoryContainer()
        setDrinkPossibilityCheckbox()
        setIntroductionEditText()
        setAddImageButton()
        setRecommendDrinkEditText()
        setRecommendMenuEditText()
        setToolbarTitle()

        setAdapter()

    }

    private fun observeStates() {

        repeatWhenUiStarted {
            viewModel.foodCategoryState.collect {
                binding.foodCategoryText.text = it.name
            }
        }

        repeatWhenUiStarted {
            viewModel.drinkPossibilityState.collect { isSelected ->
                binding.drinkPossibilityCheckbox.isSelected = isSelected
                binding.drinkPossibilityCheckboxContainer.isSelected = isSelected

                if (isSelected) binding.recommendDrinkEditText.visibility = View.VISIBLE
                else binding.recommendDrinkEditText.visibility = View.GONE
            }
        }

        repeatWhenUiStarted {
            viewModel.introductionTextState.collect { text ->
                binding.introductionTextCounter.text =
                    getString(R.string.text_counter_max_one_hundred, text.length)
            }
        }

        repeatWhenUiStarted {
            viewModel.isRecommendMenuFullState.collect {
                binding.recommendMenuEditText.visibility = if (it.not()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setFoodCategoryContainer() {
        binding.foodCategoryContainer.setOnClickListener {
            foodCategoryDialog.show(childFragmentManager, null)
        }
    }

    private fun setDrinkPossibilityCheckbox() {
        binding.drinkPossibilityCheckboxContainer.setOnClickListener {
            viewModel.setDrinkPossibilityState()
        }
    }

    private fun setIntroductionEditText() {
        binding.introductionEditText.addAfterTextChangedListener {
            if (it.length <= INTRODUCE_TEXT_MAX_LENGTH) viewModel.setIntroductionTextState(it)
            else {
                with(binding.introductionEditText) {
                    setText(it.substring(0, INTRODUCE_TEXT_MAX_LENGTH))
                    setSelection(INTRODUCE_TEXT_MAX_LENGTH)
                }
            }
        }
    }

    private fun setRecommendMenuEditText() {
        binding.recommendMenuEditText.editText.apply {

            setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                    binding.recommendMenuEditText.text.isNotEmpty()
                ) {
                    viewModel.addRecommendMenu(binding.recommendMenuEditText.text)
                    binding.recommendMenuChipGroup.addView(
                        Chip(requireContext()).apply {
                            text = binding.recommendMenuEditText.text
                        }
                    )
                    binding.recommendMenuEditText.editText.setText(String.Empty)
                }
                false
            }

            addAfterTextChangedListener {
                viewModel.setRecommendMenuTextState(it)
            }
        }
    }

    private fun setRecommendDrinkEditText() {
        binding.recommendDrinkEditText.editText.addAfterTextChangedListener {
            viewModel.setRecommendDrinkTextState(it)
        }
    }

    private fun setAddImageButton() {

        binding.selectImageCountText.text = getString(R.string.text_counter_max_ten, navArgs.imageUri?.size ?: 0)

        navArgs.imageUri?.let { images ->
            adapter.submitList(images)

            with(viewModel) {
                if (isImageButtonAnimating.value.not()) {
                    binding.selectImagesButton.run {
                        if (isImageButtonExtended.value) {
                            setIsImageButtonExtended(false)
                            animateShrinkWidth()
                        } else {
                            setIsImageButtonExtended(true)
                            animateExtendWidth()
                        }
                    }
                }
            }
        }

        binding.selectImagesButton.setOnClickListener {
            val directions = RegisterRestaurantFragmentDirections
                .actionRegisterRestaurantFragmentToMultiImagePickerFragment(
                    navArgs.restaurantLocationInfo
                )

            findNavController().navigate(directions)

        }
    }

    private fun setAdapter() {
        binding.selectedImagesRecyclerView.adapter = adapter
        binding.selectedImagesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(navArgs.restaurantLocationInfo?.placeName ?: String.Empty)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val INTRODUCE_TEXT_MAX_LENGTH = 100
    }

}