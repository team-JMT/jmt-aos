package org.gdsc.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.customimagepicker.adapter.ImageAdapter
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.view.HomeFragment.Companion.URI_LIST_CHECKED
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

class ImagePickerFragment : Fragment() {
    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)
        binding.recyclerviewImage.setOnClickListener {
            // Pass Uri list to fragment outside
            activity?.supportFragmentManager?.setFragmentResult(
                URI_LIST_CHECKED,
                bundleOf("uriList" to viewModel.getCheckedImageUriList())
            )
            findNavController().navigateUp()
        }

        val adapter = ImageAdapter(viewModel)
        binding.recyclerviewImage.adapter = adapter

        viewModel.fetchImageItemList(requireContext())
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: ImageAdapter) {
        viewModel.imageItemList.observe(viewLifecycleOwner) { imageItemList ->
            adapter.submitList(imageItemList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}