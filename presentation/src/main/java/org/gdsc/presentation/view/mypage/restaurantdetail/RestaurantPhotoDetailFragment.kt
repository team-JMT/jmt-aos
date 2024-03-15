package org.gdsc.presentation.view.mypage.restaurantdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.gdsc.domain.Empty
import org.gdsc.presentation.databinding.FragmentRestaurantPhotoDetailBinding
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.adapter.ImageSlider
import org.gdsc.presentation.view.mypage.adapter.ImageSliderItem
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

class RestaurantPhotoDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by activityViewModels()

    private val parentActivity by lazy { activity as MainActivity }

    private val imageSlider = ImageSlider(

    ) {
        with(binding.reviewItem) {
            Glide.with(root)
                .load(it.reviewerImageUrl)
                .into(ivProfile)

            tvNickname.text = it.userName
            tvContent.text = it.reviewContent
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

    }

    private fun setAdapter() {
        binding.imageSlider.adapter = imageSlider

        viewModel.reviews.value.let { reviews ->

            val imageSliderItems = reviews.map { review ->
                review.reviewImages.map { imageUrl ->
                    ImageSliderItem(imageUrl, review)
                }
            }.flatten()

            binding.imageSlider.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    parentActivity.changeToolbarTitle("${position + 1} / ${imageSliderItems.size}")
                }
            })

            imageSlider.submitList(imageSliderItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentActivity.changeToolbarTitle(String.Empty)
    }

}