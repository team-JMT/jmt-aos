package org.gdsc.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.gdsc.presentation.databinding.FragmentSignUpNicknameBinding
import org.gdsc.presentation.utils.addAfterTextChangedListener
import org.gdsc.presentation.utils.hideKeyBoard
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.utils.showKeyBoard

class SignUpNicknameFragment : Fragment() {

    private var _binding: FragmentSignUpNicknameBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyBoard(requireActivity(), binding.nicknameEditText.editText)
        setNextButton()
        setHideKeyboard()
        observeNicknameValidation()
        binding.nicknameEditText.editText.addAfterTextChangedListener {
            viewModel.updateNicknameState(it)
        }
    }

    private fun setHideKeyboard() {
        binding.root.setOnClickListener {
            hideKeyBoard(requireActivity())
        }
    }

    private fun observeNicknameValidation() {
        repeatWhenUiStarted {
            viewModel.isNicknameVerified.collect { isVerified ->
                binding.nextBtn.isEnabled = isVerified
            }
        }
    }

    private fun setNextButton() {
        binding.nextBtn.setOnClickListener {
            viewModel.checkDuplicatedNickname(
                onNicknameIsNotDuplicated = {
                    val action =
                        SignUpNicknameFragmentDirections.actionSignUpNicknameFragmentToSignUpCompleteFragment()
                    findNavController().navigate(action)
                },
                onNicknameIsDuplicated = {
                    binding.nicknameEditText.setDuplicatedNickname()
                }
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}