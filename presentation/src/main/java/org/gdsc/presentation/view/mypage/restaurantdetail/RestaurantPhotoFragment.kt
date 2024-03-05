package org.gdsc.presentation.view.mypage.restaurantdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.databinding.FragmentRestaurantPhotoBinding
import org.gdsc.presentation.model.RestaurantPhotoItem
import org.gdsc.presentation.view.mypage.adapter.RestaurantPhotoAdapter

@AndroidEntryPoint
class RestaurantPhotoFragment : Fragment() {

    private var _binding: FragmentRestaurantPhotoBinding? = null
    private val binding get() = _binding!!

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
        val restaurantPhotoAdapter = RestaurantPhotoAdapter {
            findNavController().navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToRestaurantPhotoDetailFragment())
        }

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


        // TODO: supposed to be real data
        restaurantPhotoAdapter.submitList(
            listOf(
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
                RestaurantPhotoItem("https://gdsc-jmt.s3.ap-northeast-2.amazonaws.com/profileImg/defaultImg/Default+image.png"),
            )
        )
    }

}