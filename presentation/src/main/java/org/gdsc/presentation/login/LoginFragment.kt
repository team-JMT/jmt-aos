package org.gdsc.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.gdsc.domain.model.response.UserLoginAction
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentLoginBinding
import org.gdsc.presentation.view.LoginManager
import org.gdsc.presentation.view.MainActivity

class LoginFragment : Fragment() {

    private val loginManager = LoginManager()

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = requireNotNull(_binding)
    private val viewModel: LoginViewModel by activityViewModels()

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

    // TODO: 가입 여부 확인 후 가입되어있으면 메인 화면으로 그렇지 않으면 가입 화면으로 이동
    private fun setLoginButton() {
        binding.googleLoginBtnTemplate.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                startForResult.launch(
                    IntentSenderRequest.Builder(loginManager.signInIntent(requireActivity()))
                        .build()
                )
            }
        }

        binding.googleLoginBtnText.text = context?.getString(R.string.continue_with_google)

        // TODO: Apple Login
        binding.appleLoginBtn.setOnClickListener {
            Toast.makeText(requireContext(), "준비중입니다.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private val startForResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->

                    val credential = loginManager.oneTapClient.getSignInCredentialFromIntent(intent)
                    credential.googleIdToken?.let {
                        viewModel.postSignUpWithGoogleToken(it) { tokenResponse ->
                            when(tokenResponse.userLoginAction) {
                                UserLoginAction.SIGN_UP.value -> {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToSignUpNicknameFragment()
                                    findNavController().navigate(action)
                                }
                                UserLoginAction.LOG_IN.value -> {
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }

                        }
                    }
                }
            }
        }

}