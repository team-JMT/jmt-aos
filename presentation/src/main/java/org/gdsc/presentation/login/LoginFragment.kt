package org.gdsc.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import org.gdsc.domain.model.response.UserLoginAction
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentLoginBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
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

        repeatWhenUiStarted {
            // TODO: specific handling
            viewModel.eventFlow.collect {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setLoginButton() {
        binding.googleLoginBtnTemplate.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    startForResult.launch(
                        IntentSenderRequest.Builder(loginManager.signInIntent(requireActivity()))
                            .build()
                    )
                } catch (e: ApiException) {
                    Log.e("Login", "ApiException $e")
                    showGoogleAccountRegistrationPrompt()
                } catch (e: Exception) {
                    Log.e("Login", "setLoginButton Exception $e")
                }
            }
//            showGoogleAccountRegistrationPrompt()

        }

        binding.googleLoginBtnText.text = context?.getString(R.string.continue_with_google)

        // TODO: Apple Login
        binding.appleLoginBtn.setOnClickListener {
            Toast.makeText(requireContext(), "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showGoogleAccountRegistrationPrompt() {
        Toast.makeText(requireContext(), "구글 계정을 등록해주세요.", Toast.LENGTH_SHORT).show()

        val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
        intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        startActivity(intent)
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
                            when (tokenResponse.userLoginAction) {
                                UserLoginAction.SIGN_UP.value,
                                UserLoginAction.NICKNAME_PROCESS.value -> {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToSignUpNicknameFragment()
                                    findNavController().navigate(action)
                                }

                                UserLoginAction.PROFILE_IMAGE_PROCESS.value -> {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToSignUpCompleteFragment()
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