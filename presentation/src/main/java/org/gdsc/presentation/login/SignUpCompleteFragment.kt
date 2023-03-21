package org.gdsc.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import org.gdsc.presentation.databinding.FragmentSignUpCompleteBinding

class SignUpCompleteFragment : Fragment() {

    private var _binding: FragmentSignUpCompleteBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nicknameText.text = viewModel.nicknameState.value

        binding.profileImageAddButton.setOnClickListener {
            // TODO: 갤러리에서 이미지 pick
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}