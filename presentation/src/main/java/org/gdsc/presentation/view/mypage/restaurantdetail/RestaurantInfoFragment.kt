package org.gdsc.presentation.view.mypage.restaurantdetail

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentRestaurantInfoBinding
import org.gdsc.presentation.model.RestaurantPhotoItem
import org.gdsc.presentation.utils.toPx
import org.gdsc.presentation.view.mypage.adapter.ImagePager
import org.gdsc.presentation.view.mypage.adapter.ImagePagerItem
import org.gdsc.presentation.view.mypage.adapter.RestaurantDetailPagerAdapter
import org.gdsc.presentation.view.mypage.adapter.RestaurantPhotoAdapter
import org.gdsc.presentation.view.mypage.adapter.RestaurantReviewAdapter
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantInfoFragment : Fragment() {

    private var _binding: FragmentRestaurantInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by activityViewModels()

    private val photoAdapter = ImagePager {

    }

    private val restaurantReviewAdapter = RestaurantReviewAdapter {

    }

    private val restaurantPhotoAdapter = RestaurantPhotoAdapter {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        setAdapter()
        observeData()

    }

    private fun setButtons() {
        binding.tvMoreReviews.setOnClickListener {
            (parentFragment as RestaurantDetailFragment).changeCategory(RestaurantDetailPagerAdapter.REVIEW)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restaurantInfo.collect { it ->
                it?.let { notNullRestaurantInfo ->

                    notNullRestaurantInfo.apply {
                        recommendMenu.split("#").forEach {
                            if (it.isNotBlank()) {
                                binding.cgRecommendMenu.addView(newChip(it))
                            }
                        }

                        goWellWithLiquor.split("#").forEach {
                            if (it.isNotBlank()) {
                                binding.cgRecommendDrink.addView(newChip(it))
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collect {
                restaurantReviewAdapter.submitList(it.take(2))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restaurantInfo.collect {
                it?.let { notNullRestaurantInfo ->
                    restaurantPhotoAdapter.submitList(
                        notNullRestaurantInfo.pictures.map { imageUrl ->
                            (RestaurantPhotoItem(imageUrl))
                        })
                }
            }
        }
    }

    private fun setAdapter() {

        binding.pagerPhotos.adapter = photoAdapter

        photoAdapter.submitList(
            listOf(
                ImagePagerItem("https://picsum.photos/200/200"),
                ImagePagerItem("https://picsum.photos/200/200"),
                ImagePagerItem("https://picsum.photos/200/200"),
                ImagePagerItem("https://picsum.photos/200/200"),
                ImagePagerItem("https://picsum.photos/200/200"),
                ImagePagerItem("https://picsum.photos/200/200"),
            )
        )

        binding.rvReviews.adapter = restaurantReviewAdapter
        binding.rvReviews.layoutManager = LinearLayoutManager(context)

        val spanCount = 3

        binding.rvPhotos.adapter = restaurantPhotoAdapter
        binding.rvPhotos.layoutManager = GridLayoutManager(context, spanCount)
        binding.rvPhotos.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val margin = 4.toPx
                outRect.bottom = margin
            }
        })


        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.root.width / 3
                restaurantPhotoAdapter.setSize(width, width)
                restaurantPhotoAdapter.notifyDataSetChanged()
            }
        })

    }

    private fun newChip(text: String): Chip {
        return Chip(requireContext()).apply {
            this.text = text

            chipBackgroundColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.white
            )
            chipStrokeColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.grey200
            )
            chipStrokeWidth = 1f

            this.isCheckable = false

        }
    }

}