package org.gdsc.presentation.view.mypage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.gdsc.domain.Empty
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentMyPageBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.LIKED_RESTAURANT
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.MY_REVIEW
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.REGISTERED_RESTAURANT
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserInfo()
        setPager()
        setTabLayout()
        setCollapsingToolbarOffChangedCallback()
        setMoreIcon()
        observeState()

    }

    private fun initUserInfo() {
        repeatWhenUiStarted {
            viewModel.getUserInfo()
        }
    }

    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.nicknameState.collect {
                binding.nickName.text = it
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImageState.collect {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.base_profile_image)
                    .into(binding.profileImage)
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImageState.collect {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.base_profile_image)
                    .into(binding.profileImage)
            }
        }

        repeatWhenUiStarted {
            viewModel.registeredCountState.collect {
                binding.registeredCountText.text = it.toString()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setPager() {
        binding.myPagePager.adapter = MyPagePagerAdapter(this)
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.myPagePager) { tab, position ->
            when (position) {
                REGISTERED_RESTAURANT -> tab.text = getString(R.string.registered_restaurant)
                LIKED_RESTAURANT -> tab.text = getString(R.string.liked_restaurant)
                MY_REVIEW -> tab.text = getString(R.string.my_review)
            }
        }.attach()
    }

    private fun setCollapsingToolbarOffChangedCallback() {
        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == -binding.collapsingToolbar.height) {
                repeatWhenUiStarted {
                    viewModel.nicknameState.collectLatest {
                        setToolbarTitle(it)
                    }
                }
            } else {
                setToolbarTitle()
            }
        }
    }

    private fun setMoreIcon() {
        binding.moreIcon.setOnClickListener {
            val navigation = MyPageFragmentDirections.actionMyPageFragmentToSettingsFragment()
            findNavController().navigate(navigation)
        }
    }

    private fun setToolbarTitle(title: String = String.Empty) {
        (requireActivity() as MainActivity).changeToolbarTitle(title)
    }

}