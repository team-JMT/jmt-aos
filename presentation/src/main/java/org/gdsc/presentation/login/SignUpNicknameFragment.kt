package org.gdsc.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.gdsc.presentation.databinding.FragmentSignUpNicknameBinding
import org.gdsc.presentation.utils.hideKeyBoard

class SignUpNicknameFragment : Fragment() {

    private var _binding: FragmentSignUpNicknameBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNextButton()
        setHideKeyboard()
    }

    private fun setHideKeyboard() {
        binding.root.setOnClickListener {
            hideKeyBoard(requireActivity())
        }
    }

    private fun setNextButton() {
        binding.nextBtn.setOnClickListener {
            val action = SignUpNicknameFragmentDirections.actionSignUpNicknameFragmentToSignUpCompleteFragment(binding.nicknameEditText.text)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}