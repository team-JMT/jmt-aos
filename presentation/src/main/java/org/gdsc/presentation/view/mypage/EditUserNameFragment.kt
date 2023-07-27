package org.gdsc.presentation.view.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentEditUserNameBinding
import org.gdsc.presentation.utils.ValidationUtils
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class EditUserNameFragment: Fragment() {

    private var _binding: FragmentEditUserNameBinding? = null

    val binding get() = _binding!!

    val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentEditUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle()

        binding.usernameEditText.addTextChangedListener {
            with(binding) {
                if(ValidationUtils.checkName(it.toString())) {
                    verifyIcon.setImageResource(R.drawable.cancel_icon)
                    verifyText.apply {
                        setTextColor(resources.getColor(R.color.unable_nickname_color, null))
                        text = getString(R.string.unable_nickname_cause_special_characters)
                    }
                    nextBtn.isEnabled = false

                } else {
                    verifyIcon.setImageResource(R.drawable.check_icon)
                    verifyText.apply {
                        setTextColor(resources.getColor(R.color.green500, null))
                        text = context.getString(R.string.enable_nickname)
                    }
                    nextBtn.isEnabled = true
                }

                verifyContainer.isVisible = it.isNullOrEmpty().not()
            }

        }

        binding.nextBtn.setOnClickListener {
            viewModel.checkDuplicatedNickname(
                binding.usernameEditText.text.toString(),
                onNicknameIsNotDuplicated = {
                    viewModel.updateUserName(binding.usernameEditText.text.toString()) { response ->
                        setFragmentResult("requestKey", Bundle().apply {
                            putString("bundleKey",
                                if(response.nickname == binding.usernameEditText.text.toString())
                                    "success"
                                else
                                    "fail"
                                )
                        })
                        findNavController().popBackStack()
                    }
                },
                onNicknameIsDuplicated = {
                    with(binding) {
                        verifyIcon.setImageResource(R.drawable.cancel_icon)
                        verifyText.apply {
                            setTextColor(resources.getColor(R.color.unable_nickname_color, null))
                            text = getString(R.string.unable_nickname_cause_duplicated)
                        }
                        nextBtn.isEnabled = false
                    }
                })


        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(getString(R.string.title_user_name_edit))
    }
}