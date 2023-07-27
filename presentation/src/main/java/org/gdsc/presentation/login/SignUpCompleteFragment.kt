package org.gdsc.presentation.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.presentation.databinding.FragmentSignUpCompleteBinding
import org.gdsc.presentation.utils.checkMediaPermissions
import org.gdsc.presentation.utils.findPath
import org.gdsc.presentation.utils.showMediaPermissionsDialog
import org.gdsc.presentation.view.MainActivity
import java.io.File


class SignUpCompleteFragment : Fragment() {

    private var _binding: FragmentSignUpCompleteBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val viewModel: LoginViewModel by activityViewModels()

    private val onSuccess: () -> Unit = {
        val action = SignUpCompleteFragmentDirections.actionSignUpCompleteFragmentToImagepickerFragment()
        findNavController().navigate(action)
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if(isGranted.all{ it.value }) {
            onSuccess.invoke()
        }else{
            this.showMediaPermissionsDialog()
        }
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

        binding.nicknameText.text = viewModel.nicknameState.value

        binding.profileImageAddButton.setOnClickListener {
            this.checkMediaPermissions(
                requestPermissionsLauncher
            ) { onSuccess.invoke() }
        }

        binding.nextBtn.setOnClickListener {
            viewModel.profileImageState.value.let {
                if(it.isNullOrEmpty())
                    viewModel.requestSignUpWithoutImage { moveToMain() }
                else {
                    val file = File(it.toUri().findPath(requireContext()))
                    val requestFile = RequestBody.create(MediaType.parse("image/png"), file)
                    val body =
                        MultipartBody.Part.createFormData("profileImg", file.name, requestFile)
                    viewModel.requestSignUpWithImage(body) {
                        moveToMain()
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
