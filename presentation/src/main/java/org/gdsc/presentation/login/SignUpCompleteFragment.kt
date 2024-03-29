package org.gdsc.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.presentation.BaseFragment
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentSignUpCompleteBinding
import org.gdsc.presentation.utils.BitmapUtils.getCompressedBitmapFromUri
import org.gdsc.presentation.utils.BitmapUtils.saveBitmapToFile
import org.gdsc.presentation.utils.checkMediaPermissions
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity


class SignUpCompleteFragment : BaseFragment() {

    private var _binding: FragmentSignUpCompleteBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val viewModel: LoginViewModel by activityViewModels()

    override fun grantedPermissions() {
        val action = SignUpCompleteFragmentDirections.actionSignUpCompleteFragmentToImagepickerFragment()
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileImageButtonAnimation()

        setFragmentResultListener("pickImage") { _, bundle ->
            val imageUri = bundle.getString("imageUri")
            imageUri?.let {
                viewModel.updateProfileImageState(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.profileImageState.collect {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(R.drawable.base_profile_image)
                    .into(binding.profileImage)
            }
        }

        binding.nicknameText.text = viewModel.nicknameState.value

        binding.profileImageAddButton.setOnClickListener {
            this.checkMediaPermissions(
                requestPermissionsLauncher
            ) { grantedPermissions() }
        }

        binding.nextBtn.setOnClickListener {
            viewModel.profileImageState.value.let {
                if(it.isEmpty())
                    viewModel.requestSignUpWithoutImage { moveToMain() }
                else {
                    val file = it.toUri()
                        .getCompressedBitmapFromUri(requireContext())
                        ?.saveBitmapToFile(requireContext(), "profile.jpg")

                    if (file != null) {
                        val requestFile = RequestBody.create(MediaType.parse("image/png"), file)
                        val body =
                            MultipartBody.Part.createFormData("profileImg", file.name, requestFile)
                        viewModel.requestSignUpWithImage(body) {
                            moveToMain()
                        }
                    } else {
                        // TODO: Exception Handling
                    }
                }
            }
        }

    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setProfileImageButtonAnimation() {
        binding.profileImageAddButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val parent = v.parent as CardView
                    parent.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                }

                MotionEvent.ACTION_UP -> {
                    val parent = v.parent as CardView
                    parent.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                }
            }
            false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
