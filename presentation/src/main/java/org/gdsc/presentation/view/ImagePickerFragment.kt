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
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.data.ImageItem
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.view.HomeFragment.Companion.URI_SELECTED
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

@AndroidEntryPoint
class ImagePickerFragment : Fragment(), GalleryImageClickListener {
    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        val adapter = ImageAdapter(viewModel)
        adapter.setListener(this)
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

    override fun onImageClick(imageItem: ImageItem) {
        activity?.supportFragmentManager?.setFragmentResult(
            URI_SELECTED,
            bundleOf("uri" to imageItem.uri, "bucket" to imageItem.bucket)
        )
        findNavController().navigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}