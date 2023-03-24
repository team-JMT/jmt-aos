package org.gdsc.presentation.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.permissionButton.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeToPermission()
            findNavController().navigate(directions)
        }

        // After getting Uri list selected from ImagePicker
        activity?.supportFragmentManager?.setFragmentResultListener(URI_SELECTED, viewLifecycleOwner) { _, bundle ->
            bundle.getString("bucket")?.let { bucket ->
                Log.d(TAG, bucket)
            }
            bundle.getString("uri")?.let { uri ->
                Log.d(TAG, uri)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val URI_SELECTED = "URI_SELECTED"
        const val TAG = "HomeFragment"
    }
}