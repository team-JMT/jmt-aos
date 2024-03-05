package org.gdsc.presentation.view.mypage.restaurantdetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentRestaurantDetailBinding
import org.gdsc.presentation.utils.CalculatorUtils
import org.gdsc.presentation.view.mypage.adapter.RestaurantDetailPagerAdapter
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        setTabLayout()
        observeData()
    }

    private fun setButtons() {
        binding.tvCopy.setOnClickListener {

            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val clipData = ClipData.newPlainText("address", binding.tvAddress.text)
            clipboardManager.setPrimaryClip(clipData)

        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.restaurantInfo.collect {
                it?.let { notNullRestaurantInfo ->

                    notNullRestaurantInfo.apply {
                        binding.tvRestaurantName.text = name
                        binding.tvDistance.text = requireContext().getString(
                            R.string.distance_from_current_location,
                            CalculatorUtils.getDistanceWithLength(it.differenceInDistance.toInt())
                        )
                        binding.tvAddress.text = address
                        binding.tvCategory.text = category
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorInfo.collect {
                it?.let { notNullAuthorInfo ->

                    notNullAuthorInfo.apply {
                        binding.tvNickname.text = nickname
                        Glide.with(binding.root)
                            .load(profileImg)
                            .into(binding.ivProfile)
                    }
                }
            }
        }
    }

    private fun setTabLayout() {

        binding.restaurantDetailPager.adapter = RestaurantDetailPagerAdapter(this)
        binding.restaurantDetailPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.restaurantDetailPager) { tab, position ->
            when (position) {
                RestaurantDetailPagerAdapter.RESTAURANT_INFO -> tab.text = "식당 정보"
                RestaurantDetailPagerAdapter.PHOTO -> tab.text = "사진"
                RestaurantDetailPagerAdapter.REVIEW -> tab.text = "후기"
            }
        }.attach()

    }

    fun changeCategory(category: Int) {
        binding.restaurantDetailPager.currentItem = category
    }


}