package org.gdsc.presentation.view.allsearch

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchCategoryPagerAdapter(
    fragment: Fragment,
    private val keyword: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = SEARCH_CATEGORY_PAGER_SIZE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CATEGORY_RESTAURANT -> SearchCategoryRestaurantFragment(keyword)
            CATEGORY_GROUP -> SearchCategoryGroupFragment(keyword)
            else -> SearchCategoryAllFragment(keyword)
        }
    }

    companion object {
        private const val SEARCH_CATEGORY_PAGER_SIZE = 3

        const val CATEGORY_ALL = 0
        const val CATEGORY_RESTAURANT = 1
        const val CATEGORY_GROUP = 2
    }
}