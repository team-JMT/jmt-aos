package org.gdsc.presentation.view.mypage.restaurantdetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.domain.Empty
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentRestaurantDetailBinding
import org.gdsc.presentation.utils.BitmapUtils.getCompressedBitmapFromUri
import org.gdsc.presentation.utils.BitmapUtils.saveBitmapToFile
import org.gdsc.presentation.utils.CalculatorUtils
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.adapter.PhotoWillBeUploadedAdapter
import org.gdsc.presentation.view.mypage.adapter.RestaurantDetailPagerAdapter
import org.gdsc.presentation.view.mypage.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestaurantDetailViewModel by activityViewModels()

    private val parentActivity by lazy { activity as MainActivity }

    private val navArgs by navArgs<RestaurantDetailFragmentArgs>()

    private val adapter = PhotoWillBeUploadedAdapter {
        viewModel.deletePhotoForReviewState(it)
    }

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

        repeatWhenUiStarted {
            viewModel.photosForReviewState.collect {
                adapter.submitList(it)
            }
        }

        binding.topScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > binding.tvRestaurantName.height) {
                    parentActivity.changeToolbarTitle(binding.tvRestaurantName.text.toString())
                } else {
                    parentActivity.changeToolbarTitle(String.Empty)
                }
            })

        binding.rvImageListWillBeUploaded.adapter = adapter

        binding.addImageIcon.setOnClickListener {
            val directions =
                RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToMultiImagePickerFragment()

            findNavController().navigate(directions)
        }

        binding.btnRegister.setOnClickListener {

            val pictures = mutableListOf<MultipartBody.Part>()

            viewModel.photosForReviewState.value.forEachIndexed { index, sUri ->

                sUri.toUri()
                    .getCompressedBitmapFromUri(requireContext())
                    ?.saveBitmapToFile(requireContext(), "$index.jpg")?.let { imageFile ->

                        val requestFile =
                            RequestBody.create(
                                MediaType.parse("image/png"),
                                imageFile
                            )

                        val body =
                            MultipartBody.Part.createFormData(
                                "reviewImages",
                                imageFile.name,
                                requestFile
                            )

                        pictures.add(body)

                    }

            }

            viewModel.postReview(
                binding.etReview.text.toString(),
                pictures
            ) {
                binding.etReview.text.clear()
                Toast.makeText(requireContext(), "후기가 등록되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }

        setFragmentResultListener("pickImages") { _, bundle ->
            val images = bundle.getStringArray("imagesUri")
            viewModel.setPhotosForReviewState(images?.toList() ?: emptyList())

            if (images.isNullOrEmpty()) return@setFragmentResultListener
        }
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
            viewModel.init(navArgs.restaurantId)
        }

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
        binding.restaurantDetailPager.offscreenPageLimit = 1

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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDataCleared()
    }
}