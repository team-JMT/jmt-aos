package org.gdsc.presentation.view.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.domain.Empty
import org.gdsc.domain.model.UserInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSettingsBinding
import org.gdsc.presentation.utils.findPath
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.JmtSnackbar
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel
import java.io.File

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

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

        initUserInfo()
        setToolbarTitle()
        observeState()

        binding.profileImageButton.setOnClickListener {
            setFragmentResultListener("pickImage") { _, bundle ->
                val result = bundle.getString("imageUri")
                result?.let {
                    val file = File(it.toUri().findPath(requireContext()))
                    val requestFile = RequestBody.create(MediaType.parse("image/png"), file)
                    val body =
                        MultipartBody.Part.createFormData("profileImg", file.name, requestFile)

                    viewModel.updateProfileImage(body) {
                        viewModel.updateProfileImageState(it)
                    }
                }?: run {
                    viewModel.updateDefaultProfileImage {
                        viewModel.updateProfileImageState(String.Empty)
                    }
                }
            }

            val navigate = SettingsFragmentDirections.actionSettingsFragmentToImagePickerFragment()
            findNavController().navigate(navigate)
        }

        binding.btnChangeUserName.setOnClickListener {
            setFragmentResultListener("requestKey") { _, bundle ->
                val result = bundle.getString("bundleKey")
                when(result) {
                    "success" -> {
                        JmtSnackbar.make(
                            binding.root,
                            getString(R.string.enable_nick_name_change),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "fail" -> {
                        JmtSnackbar.make(
                            binding.root,
                            getString(R.string.unable_nick_name_change),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val navigate = SettingsFragmentDirections.actionSettingsFragmentToEditUserNameFragment()
            findNavController().navigate(navigate)
        }

        binding.tvAccountManagement.setOnClickListener {
            val navigate = SettingsFragmentDirections.actionSettingsFragmentToAccountManagementFragment()
            findNavController().navigate(navigate)
        }
    }

    private fun initUserInfo() {
        repeatWhenUiStarted {
            viewModel.getUserInfo()
        }
    }

    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.nicknameState.collect {
                binding.tvUserName.text = it
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImageState.collect {
                initProfileImage(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.emailState.collect {
                binding.tvUserEmal.text = it
            }
        }

    }

    private fun initProfileImage(profileImg: String) {
        Glide.with(this)
            .load(profileImg)
            .placeholder(R.drawable.base_profile_image)
            .into(binding.profileImage)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(String.Empty)
    }
}