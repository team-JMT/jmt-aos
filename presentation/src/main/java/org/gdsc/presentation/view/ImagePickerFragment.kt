package org.gdsc.presentation.view

import android.content.Context
import android.content.DialogInterface
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
import org.gdsc.presentation.R
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.adapter.ImageAdapter
import org.gdsc.presentation.data.ImageItem
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.view.HomeFragment.Companion.URI_SELECTED
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

@AndroidEntryPoint
class ImagePickerFragment : Fragment(), GalleryImageClickListener {
    private lateinit var callback: OnBackPressedCallback

    private val directions = ImagePickerFragmentDirections.actionImagepickerToHome()

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val imagePickerViewModel: ImagePickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        setupActionBar()
        setupMenu()
        setupAdapter()

        CoroutineScope(Dispatchers.IO).launch {
            imagePickerViewModel.fetchImageItemList()
        }

        // 갤러리 선택용 다이얼로그
        binding.galleryButton.setOnClickListener {
            val array = imagePickerViewModel.getGalleryAlbum().toTypedArray()

            AlertDialog.Builder(this.requireContext())
                .setItems(array) { _, which ->
                    val currentItem = array[which]
                    imagePickerViewModel.galleryName.value = currentItem
                }.show()
        }

        return binding.root
    }

    // 갤러리 이미지 불러오기 및 구독
    private fun initGallery(adapter: ImageAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                imagePickerViewModel.imageItemListFlow.collect {
                    adapter.submitList(it)
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

    private fun setupAdapter() {
        val adapter = ImageAdapter(imagePickerViewModel)
        adapter.setListener(this)
        binding.recyclerviewImage.adapter = adapter
        initGallery(adapter)
    }
    private fun setupActionBar() {
        (activity as SetProfileActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (activity as SetProfileActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.finish)
    }
    private fun setupMenu() {
        (requireActivity() as SetProfileActivity).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.tolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    android.R.id.home -> {
                        callback.handleOnBackPressed()
                        return true
                    }
                }
                return true
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachBackPressedCallback()
    }

    private fun attachBackPressedCallback() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(directions)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}