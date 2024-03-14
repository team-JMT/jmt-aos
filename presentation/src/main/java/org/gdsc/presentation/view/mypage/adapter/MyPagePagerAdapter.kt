package org.gdsc.presentation.view.mypage.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.gdsc.presentation.view.mypage.MyReviewFragment
import org.gdsc.presentation.view.mypage.RegisteredRestaurantFragment

class MyPagePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = MY_PAGE_PAGER_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            REGISTERED_RESTAURANT -> RegisteredRestaurantFragment()
//            LIKED_RESTAURANT -> LikedRestaurantFragment()
            else -> MyReviewFragment()
        }
    }

    companion object {
        private const val MY_PAGE_PAGER_SIZE = 2 // 3

        const val REGISTERED_RESTAURANT = 0
//        const val LIKED_RESTAURANT = 1
        const val MY_REVIEW = 1 // 2
    }
}