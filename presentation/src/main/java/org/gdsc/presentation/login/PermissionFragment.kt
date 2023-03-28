package org.gdsc.presentation.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import org.gdsc.presentation.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {
    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    // 안드로이드 13용 갤러리 퍼미션
    private val requiredPermissionsTIRAMISU = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    private val requiredPermissionsOTHER = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    // 권한 확인 완료 후 ImagePickerFragment로 이동
    private val onSuccess: () -> Unit = {
        val action = PermissionFragmentDirections.actionPermissionFragmentToImagepickerFragment()
        findNavController().navigate(action)
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        if(isGranted.all{ it.value }) {
            onSuccess.invoke()
        }else{
            binding.imagepickerButton.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Accept permissions to use this application", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imagepickerButton.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${activity?.packageName}")
            )
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            checkPermission(requiredPermissionsTIRAMISU)
        } else{
            checkPermission(requiredPermissionsOTHER)
        }
    }
    private fun checkPermission(requiredPermissions:Array<String>) {
        if(hasAllPermissions(requireContext(), requiredPermissions)) {
            onSuccess.invoke()
        }else{
            requestPermissionsLauncher.launch(requiredPermissions)
        }
    }

    private fun hasAllPermissions(context: Context, requiredPermissions: Array<String>) = requiredPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}