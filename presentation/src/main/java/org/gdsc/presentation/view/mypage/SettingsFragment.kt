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
import com.naver.maps.map.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.domain.Empty
import org.gdsc.domain.model.UserInfo
import org.gdsc.presentation.BaseFragment
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ContentSheetChoiceImageBinding
import org.gdsc.presentation.databinding.FragmentSettingsBinding
import org.gdsc.presentation.model.ResultState
import org.gdsc.presentation.utils.checkMediaPermissions
import org.gdsc.presentation.utils.findPath
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.BottomSheetDialog
import org.gdsc.presentation.view.custom.JmtSnackbar
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel
import java.io.File

@AndroidEntryPoint
class SettingsFragment: BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    override fun grantedPermissions() {
        setImagePickerFragmentResultListener()

        val navigate = SettingsFragmentDirections.actionSettingsFragmentToImagePickerFragment()
        findNavController().navigate(navigate)
    }

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
        initVersion()

        binding.profileImageButton.setOnClickListener {
            BottomSheetDialog(requireContext())
                .bindBuilder(
                    ContentSheetChoiceImageBinding.inflate(LayoutInflater.from(requireContext()))
                ) { dialog ->
                    with(dialog) {
                        albumBtn.setOnClickListener {
                            this@SettingsFragment.checkMediaPermissions(
                                requestPermissionsLauncher
                            ) { grantedPermissions() }

                            dismiss()
                        }
                        defaultImageBtn.setOnClickListener {
                            updateDefaultProfileImage()
                            dismiss()
                        }

                        show()
                    }
                }
        }

        binding.btnChangeUserName.setOnClickListener {
            setChangeNameFragmentResultListener()

            val navigate = SettingsFragmentDirections.actionSettingsFragmentToEditUserNameFragment()
            findNavController().navigate(navigate)
        }

        binding.tvAccountManagement.setOnClickListener {
            val navigate = SettingsFragmentDirections.actionSettingsFragmentToAccountManagementFragment()
            findNavController().navigate(navigate)
        }
    }

    // 업데이트는 플레이스토어에 출시 이후에 추가해주기
    private fun initVersion() {
        binding.tvVersionInfo.text = BuildConfig.VERSION_NAME + " v"
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

    private fun setImagePickerFragmentResultListener() {
        setFragmentResultListener("pickImage") { _, bundle ->
            val result = bundle.getString("imageUri")
            result?.let {
                val file = File(it.toUri().findPath(requireContext()))
                val requestFile = RequestBody.create(MediaType.parse("image/png"), file)
                val body =
                    MultipartBody.Part.createFormData("profileImg", file.name, requestFile)

                viewModel.updateProfileImage(body) { result ->
                    when(result) {
                        is ResultState.OnSuccess -> {
                            viewModel.updateProfileImageState(result.response)
                            showSuccessSnackbar(R.string.enable_profile_image_change)
                        }
                        is ResultState.OnRemoteError -> {
                            showFailSnackbar(R.string.unable_profile_image_change)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun updateDefaultProfileImage() {
        viewModel.updateDefaultProfileImage { result ->
            when(result) {
                is ResultState.OnSuccess -> {
                    viewModel.updateProfileImageState(result.response)
                    showSuccessSnackbar(R.string.enable_profile_image_change)
                }
                is ResultState.OnRemoteError -> {
                    showFailSnackbar(R.string.unable_profile_image_change)
                }
                else -> {}
            }
        }
    }

    private fun setChangeNameFragmentResultListener() {
        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getString("bundleKey")
            when(result) {
                "success" -> {
                    showSuccessSnackbar(R.string.enable_nick_name_change)
                }
                "fail" -> {
                    showFailSnackbar(R.string.unable_nick_name_change)
                }
            }
        }
    }

    private fun initProfileImage(profileImg: String) {
        Glide.with(this)
            .load(profileImg)
            .placeholder(R.drawable.base_profile_image)
            .into(binding.profileImage)
    }

    private fun showSuccessSnackbar(resId: Int) {
        JmtSnackbar.make(
            binding.root,
            requireContext().getString(resId),
            Toast.LENGTH_SHORT
        )
            .setIcon(R.drawable.check_icon)
            .setTextColor(requireContext().getColor(R.color.grey800))
            .show()
    }

    private fun showFailSnackbar(resId: Int) {
        JmtSnackbar.make(
            binding.root,
            requireContext().getString(resId),
            Toast.LENGTH_SHORT
        )
            .setIcon(R.drawable.cancel_icon)
            .setTextColor(requireContext().getColor(R.color.grey800))
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(String.Empty)
    }
}