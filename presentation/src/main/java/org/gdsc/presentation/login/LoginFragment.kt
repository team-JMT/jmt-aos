package org.gdsc.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoginButton()
    }

    private fun setLoginButton() {
        // TODO: 가입 여부 확인 후 가입되어있으면 메인 화면으로 그렇지 않으면 가입 화면으로 이동
        binding.googleLoginBtn.apply {

            val child = getChildAt(0) as? TextView
            child?.text = context.getString(R.string.continue_with_google)

            setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpNicknameFragment()
                findNavController().navigate(action)
            }
        }

        // TODO: 가입 여부 확인 후 가입되어있으면 메인 화면으로 그렇지 않으면 가입 화면으로 이동
        binding.appleLoginBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpNicknameFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}