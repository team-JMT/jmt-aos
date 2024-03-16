package org.gdsc.presentation.view.restaurantregistration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSearchRestaurantLocationInfoBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.JmtSnackbar
import org.gdsc.presentation.view.restaurantregistration.adapter.RestaurantLocationInfoAdapter
import org.gdsc.presentation.view.restaurantregistration.viewmodel.SearchRestaurantLocationInfoViewModel

@AndroidEntryPoint
class SearchRestaurantLocationInfoFragment : Fragment() {

    private var _binding: FragmentSearchRestaurantLocationInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchRestaurantLocationInfoViewModel by viewModels()

    private val navArgs: SearchRestaurantLocationInfoFragmentArgs by navArgs()
    private val groupId by lazy { navArgs.groupId }

    private val adapter by lazy {
        RestaurantLocationInfoAdapter {
            viewLifecycleOwner.lifecycleScope.launch {
                val canRegister = viewModel.canRegisterRestaurant(it.id)

                val action = SearchRestaurantLocationInfoFragmentDirections
                    .actionSearchRestaurantLocationInfoFragmentToConfirmRestaurantRegistrationFragment(
                        groupId,
                        canRegister,
                        it,
                    )
                findNavController().navigate(action)
            }
        }
    }

    private var debounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRestaurantLocationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setToolbarTitle()

        setSearchEditTextDebounceWatcher {
            if (binding.restaurantNameEditText.text.isNotEmpty()) {

                setVisibleStatusContainer(false)

                viewLifecycleOwner.lifecycleScope.launch {
                    val currentLocation = viewModel.getCurrentLocation()

                    if (currentLocation != null) {
                        // TODO: Paging
                        val response =
                            viewModel.getRestaurantLocationInfo(
                                binding.restaurantNameEditText.text,
                                currentLocation.latitude.toString(),
                                currentLocation.longitude.toString(),
                                1
                            )

                        if (response.isNotEmpty()) {
                            // TODO: Error Handling
                            adapter.setTargetString(binding.restaurantNameEditText.text)
                            adapter.submitList(response)
                        } else {
                            setVisibleStatusContainer(true)
                            setStatusImage(R.drawable.jmt_normal_character)
                            setStatusContainerText(getString(R.string.no_searched_result))
                            adapter.submitList(emptyList())
                        }


                    } else {
                        JmtSnackbar.make(
                            binding.root,
                            getString(R.string.get_location_error),
                            Snackbar.LENGTH_SHORT
                        )
                            .setTextColor(R.color.unable_nickname_color).show()
                    }

                }
            } else {
                setVisibleStatusContainer(true)
                setStatusImage(R.drawable.jmt_wink_character)
                setStatusContainerText(getString(R.string.search_for_recommendable_restaurant))
                adapter.submitList(emptyList())
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (adapter.currentList.isEmpty()) {
            setVisibleStatusContainer(true)
            setStatusImage(R.drawable.jmt_wink_character)
            setStatusContainerText(getString(R.string.search_for_recommendable_restaurant))
        } else {
            setVisibleStatusContainer(false)
        }
    }

    private fun setAdapter() {
        binding.restaurantInfoRecyclerView.adapter = adapter
        binding.restaurantInfoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setSearchEditTextDebounceWatcher(delay: Long = 500L, block: () -> Unit) {
        binding.restaurantNameEditText.editText.addAfterTextChangedListener {
            debounceJob?.cancel()
            debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(delay)
                block()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        debounceJob?.cancel()
        super.onDestroyView()
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle("맛집등록")
    }

    private fun setVisibleStatusContainer(isVisible: Boolean) {
        binding.statusContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setStatusImage(@DrawableRes imageResId: Int) {
        binding.statusImage.setImageResource(imageResId)
    }

    private fun setStatusContainerText(text: String) {
        binding.statusText.text = text
    }
}