package org.gdsc.presentation.view.mypage.restaurantdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.databinding.FragmentRestaurantPhotoBinding
import org.gdsc.presentation.model.RestaurantPhotoItem
import org.gdsc.presentation.view.mypage.adapter.RestaurantPhotoAdapter
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantPhotoFragment : Fragment() {

    private var _binding: FragmentRestaurantPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<RestaurantDetailViewModel>()

    private val restaurantPhotoAdapter = RestaurantPhotoAdapter {
        findNavController().navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToRestaurantPhotoDetailFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun setAdapter() {

        val spanCount = 3

        binding.rvPhotos.adapter = restaurantPhotoAdapter
        binding.rvPhotos.layoutManager = GridLayoutManager(context, spanCount)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width / 3
                restaurantPhotoAdapter.setSize(width, width)
                restaurantPhotoAdapter.notifyDataSetChanged()
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collect { reviews ->
                restaurantPhotoAdapter.submitList(
                    reviews.map { it.reviewImages }.flatten().map { imageUrl ->
                        (RestaurantPhotoItem(imageUrl))
                    })
            }
        }

    }

}