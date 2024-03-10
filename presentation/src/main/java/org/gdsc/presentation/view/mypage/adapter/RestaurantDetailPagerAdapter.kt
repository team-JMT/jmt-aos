package org.gdsc.presentation.view.mypage.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.gdsc.presentation.view.mypage.restaurantdetail.RestaurantInfoFragment
import org.gdsc.presentation.view.mypage.restaurantdetail.RestaurantPhotoFragment
import org.gdsc.presentation.view.mypage.restaurantdetail.RestaurantReviewFragment

class RestaurantDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = RESTAURANT_DETAIL_PAGER_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            RESTAURANT_INFO -> RestaurantInfoFragment()
            PHOTO -> RestaurantPhotoFragment()
            else -> RestaurantReviewFragment()
        }
    }

    companion object {
        private const val RESTAURANT_DETAIL_PAGER_SIZE = 3

        const val RESTAURANT_INFO = 0
        const val PHOTO = 1
        const val REVIEW = 2
    }
}