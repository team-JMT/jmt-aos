package org.gdsc.presentation.view.mypage.restaurantdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.FragmentRestaurantReviewBinding
import org.gdsc.presentation.view.mypage.adapter.RestaurantReviewAdapter
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantReviewFragment : Fragment() {

    private var _binding: FragmentRestaurantReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by activityViewModels()

    private val restaurantReviewAdapter = RestaurantReviewAdapter {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collect {
                restaurantReviewAdapter.submitList(it)
            }
        }
    }

    private fun setAdapter() {

        binding.rvReviews.adapter = restaurantReviewAdapter
        binding.rvReviews.layoutManager = LinearLayoutManager(context)

    }

}