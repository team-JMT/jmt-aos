package org.gdsc.presentation.view

import android.content.DialogInterface
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.adapter.ImageAdapter
import org.gdsc.presentation.data.ImageItem
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.view.HomeFragment.Companion.URI_SELECTED
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

@AndroidEntryPoint
class ImagePickerFragment : Fragment(), GalleryImageClickListener {
    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val imagePickerViewModel: ImagePickerViewModel by viewModels()

    private var galleryName:String = "Camera"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        val adapter = ImageAdapter(imagePickerViewModel)
        adapter.setListener(this)
        binding.recyclerviewImage.adapter = adapter
        initGallery(adapter)
        CoroutineScope(Dispatchers.IO).launch {
            imagePickerViewModel.fetchImageItemList()
        }

        binding.galleryButton.setOnClickListener {
            val array = imagePickerViewModel.getGalleryAlbum().toTypedArray()

            AlertDialog.Builder(this.requireContext())
                .setTitle("list")
                .setItems(array) { _, which ->
                    val currentItem = array[which]
                    galleryName = currentItem
                }.show()
        }

        return binding.root
    }

    private fun initGallery(adapter: ImageAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                imagePickerViewModel.imageItemListFlow.collect {
                    adapter.submitList(filterImagesByName(it, galleryName))
                }
            }
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

    fun filterImagesByName(images: MutableList<ImageItem>, name: String): MutableList<ImageItem> {
        return images.filter {
            it.bucket == name
        } as MutableList<ImageItem>
    }

}