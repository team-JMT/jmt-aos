package org.gdsc.presentation.view.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.domain.Empty
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentMyPageBinding
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.LIKED_RESTAURANT
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.MY_REVIEW
import org.gdsc.presentation.view.mypage.adapter.MyPagePagerAdapter.Companion.REGISTERED_RESTAURANT

@AndroidEntryPoint
class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPager()
        setTabLayout()
        setCollapsingToolbarOffChangedCallback()
        setMoreIcon()

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
                // TODO: User Nickname From ViewModel
                setToolbarTitle("Treenamu")
            } else {
                setToolbarTitle()
            }
        }
    }

    private fun setMoreIcon() {
        binding.moreIcon.setOnClickListener {
            // TODO: 프로필 수정 화면으로 이동
            Toast.makeText(requireContext(), "TODO: 프로필 수정 화면", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setToolbarTitle(title: String = String.Empty) {
        (requireActivity() as MainActivity).changeToolbarTitle(title)
    }

}