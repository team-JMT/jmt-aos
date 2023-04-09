package org.gdsc.presentation.view.restaurantregistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.FragmentSearchRestaurantLocationInfoBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener

@AndroidEntryPoint
class SearchRestaurantLocationInfoFragment : Fragment() {

    private var _binding: FragmentSearchRestaurantLocationInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchRestaurantLocationInfoViewModel by viewModels()
    private val adapter by lazy { RestaurantLocationInfoAdapter{
        viewLifecycleOwner.lifecycleScope.launch {
            val canRegister = viewModel.canRegisterRestaurant(it.id)

            val action = SearchRestaurantLocationInfoFragmentDirections
                .actionSearchRestaurantLocationInfoFragmentToConfirmRestaurantRegistrationFragment(canRegister, it)
            findNavController().navigate(action)
        }
    } }

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

        setSearchEditTextDebounceWatcher {
            if (binding.restaurantNameEditText.text.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    // TODO: Paging
                    val response =
                        viewModel.getRestaurantLocationInfo(binding.restaurantNameEditText.text, 1)

                    // TODO: Error Handling
                    adapter.setTargetString(binding.restaurantNameEditText.text)
                    adapter.submitList(response)
                }
            } else {
                adapter.submitList(emptyList())
            }

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
}