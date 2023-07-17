package org.gdsc.presentation.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.domain.Empty
import org.gdsc.domain.model.UserInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSettingsBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    private var user: UserInfo = UserInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.getUserInfo()
        }

        viewLifecycleOwner.repeatWhenUiStarted {
            viewModel.userInfoState.collect {
                user = it
                initUserInfo()
            }
        }
    }

    private fun initUserInfo() {
        Glide.with(this)
            .load(
                if(user.profileImg.isNullOrEmpty()) user.profileImg
                else user.profileImg
            )
            .placeholder(R.drawable.base_profile_image)
            .into(binding.profileImage)

        binding.tvUserName.text = user.nickname
        binding.tvUserEmal.text = user.email
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(String.Empty)
    }
}