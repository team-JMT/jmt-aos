package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.gdsc.presentation.R
import org.gdsc.presentation.base.CancelViewListener
import org.gdsc.presentation.base.SearchViewListener
import org.gdsc.presentation.databinding.FragmentAllSearchContainerBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted

@AndroidEntryPoint
class AllSearchContainerFragment: Fragment() {

    private var _binding: FragmentAllSearchContainerBinding? = null
    private val binding get() = _binding!!

    val viewModel: AllSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSearchContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.setSearchViewListener(searchListener)
        binding.searchBar.setCancelViewListener(cancelViewListener)

        arguments?.getString("keyword")?.let {
            viewModel.setSearchKeyword(it)
            binding.searchBar.setSearchText(it)
        }

        setPager()
        setTabLayout()
    }

    private fun setPager() {
        binding.searchCategoryPager.adapter = SearchCategoryPagerAdapter(
            this,
            viewModel.searchKeyword.value
        )
        repeatWhenUiStarted {
            viewModel.searchKeyword.collect {
                binding.searchCategoryPager.adapter = SearchCategoryPagerAdapter(this, it)
            }
        }
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.searchCategoryPager) { tab, position ->
            when (position) {
                SearchCategoryPagerAdapter.CATEGORY_ALL -> tab.text = getString(R.string.search_category_all)
                SearchCategoryPagerAdapter.CATEGORY_RESTAURANT -> tab.text = getString(R.string.search_category_restaurant)
                SearchCategoryPagerAdapter.CATEGORY_GROUP -> tab.text = getString(R.string.search_category_group)
            }
        }.attach()
    }

    private val searchListener = object : SearchViewListener {
        override fun onSearchClear() {}
        override fun changeFocus(focus: Boolean) {}
        override fun onChangeText(text: CharSequence) {}
        override fun onSubmitText(text: CharSequence) {
            if (text.isEmpty()) return
            viewModel.setSearchKeyword(text.toString())
            binding.searchBar.setSearchText(text.toString())
        }
    }
    private val cancelViewListener = object : CancelViewListener {
        override fun onCancel() {
            findNavController().navigateUp()
        }
    }
}