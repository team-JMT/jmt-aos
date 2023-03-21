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
            //val directions = HomeFragmentDirections.actionHomeToPermission()
            findNavController().navigate(R.id.action_home_to_permission)
        }
        /*
        binding.buttonListener = View.OnClickListener {
            val directions = HomeFragmentDirections.actionHomeToPermission()
            findNavController().navigate(directions)
        }

         */

        // After getting Uri list selected from ImagePicker
        activity?.supportFragmentManager?.setFragmentResultListener(URI_LIST_CHECKED, viewLifecycleOwner) { _, bundle ->
            bundle.getStringArrayList("uriList")?.let { uriList ->
                for(uri in uriList) {
                    Log.d(TAG, uri.toString())
                    // TODO: Whatever you want!
                }
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
        const val URI_LIST_CHECKED = "URI_LIST_CHECKED"
        const val TAG = "HomeFragment"
    }
}