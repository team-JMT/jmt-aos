package org.gdsc.presentation.view.allsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.gdsc.domain.model.GroupInfo
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.FragmentSearchCategoryAllBinding
import org.gdsc.presentation.databinding.ItemSearchGroupBinding
import org.gdsc.presentation.databinding.ItemSearchRestaurantBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryGroupPreviewAdapter
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryRestaurantAdapter
import org.gdsc.presentation.view.allsearch.adapter.SearchCategoryRestaurantPreviewAdapter

@AndroidEntryPoint
class SearchCategoryAllFragment(
    private val searchKeyword: String
) : Fragment() {
): Fragment(), ViewHolderBindListener {

    private var _binding: FragmentSearchCategoryAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllSearchViewModel by activityViewModels()

    private val searchCategoryRestaurantPreviewAdapter = SearchCategoryRestaurantPreviewAdapter(this)
    private val searchCategoryGroupPreviewAdapter = SearchCategoryGroupPreviewAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCategoryAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatWhenUiStarted {
            viewModel.isForGroup.collect { isForGroup ->
                if (isForGroup.not()) {
                    binding.restaurantRecyclerView.visibility = View.GONE
                    binding.warningNoRestaurant.root.visibility = View.VISIBLE
                } else {
                    binding.restaurantRecyclerView.visibility = View.VISIBLE
                    binding.warningNoRestaurant.root.visibility = View.GONE
                }
            }
        }

        repeatWhenUiStarted {
            viewModel.searchedGroupPreviewState.collect {
                if (it.isEmpty()) {
                    binding.groupRecyclerView.visibility = View.GONE
                    binding.warningNoGroup.root.visibility = View.VISIBLE
                } else {
                    binding.groupRecyclerView.visibility = View.VISIBLE
                    binding.warningNoGroup.root.visibility = View.GONE

                }
            }
        }

        viewModel.searchRestaurantPreviewWithKeyword()
        viewModel.searchGroupPreviewWithKeyword()

        binding.restaurantRecyclerView.adapter = searchCategoryRestaurantPreviewAdapter
        binding.restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.groupRecyclerView.adapter = searchCategoryGroupPreviewAdapter
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Todo: New Adapter With Real APi
        binding.recommendedRestaurantRecyclerView.adapter = searchCategoryRestaurantPreviewAdapter
        binding.recommendedRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.icRestaurant.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.search_category_pager)
            viewPager.currentItem = 1
        }

        viewModel.setSearchKeyword(searchKeyword)
        observeState()
    }

    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.searchedRestaurantPreviewState.collect {
                searchCategoryRestaurantPreviewAdapter.submitList(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.searchedGroupPreviewState.collect {
                searchCategoryGroupPreviewAdapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewHolderBind(holder: BaseViewHolder<out ViewBinding>, _item: Any) {
        if (holder is SearchCategoryRestaurantPreviewAdapter.RestaurantsWithSearchPreviewViewHolder && _item is RegisteredRestaurant) {
            val binding = ItemSearchRestaurantBinding.bind(holder.itemView)
            binding.run {
                Glide.with(root)
                    .load(_item.userProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(userProfileImage)

                groupName.text = _item.groupName
                userName.text = _item.userNickName

                Glide.with(root)
                    .load(_item.restaurantImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(restaurantImage)

                restaurantCategory.text = _item.category
                restaurantName.text = _item.name
            }
            holder.itemView.setOnClickListener {
                findNavController().navigate(
                    AllSearchContainerFragmentDirections.actionAllSearchContainerFragmentToRestaurantDetailFragment(
                        _item.id
                    )
                )
            }
        } else if (holder is SearchCategoryGroupPreviewAdapter.GroupWithSearchPreviewViewHolder && _item is GroupPreview) {
            val binding = ItemSearchGroupBinding.bind(holder.itemView)
            binding.run {
                Glide.with(root)
                    .load(_item.groupProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(ivGroupImage)

                tvGroupName.text = _item.groupName
                tvIntroduction.text = _item.groupIntroduce
                tvMemberCount.text = _item.memberCnt.toString()
                tvRestaurantCount.text = _item.restaurantCnt.toString()
            }
            holder.itemView.setOnClickListener {
                findNavController().navigate(
                    AllSearchContainerFragmentDirections.actionAllSearchContainerFragmentToMyGroupGroupDetail(
                        "group-detail/${_item.groupId}"
                    )
                )
            }
        }
    }
}