package org.gdsc.presentation.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private lateinit var popupMenu:PopupMenu

    private val directions = ImagePickerFragmentDirections.actionImagepickerToHome()

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val imagePickerViewModel: ImagePickerViewModel by viewModels()

    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        setupActionBar()
        setupMenu()
        setupAdapter()

        // 갤러리 선택용 다이얼로그
        binding.galleryButton.setOnClickListener {view ->
            setupPopUpMenu(view)
            popupMenu.show()
        }

        return binding.root
    }
    private fun setupPopUpMenu(view: View) {
        val albumNameList = imagePickerViewModel.getGalleryAlbum().toTypedArray()

        popupMenu = PopupMenu(requireContext(), view)
        (activity as SetProfileActivity).menuInflater.inflate(R.menu.sample_menu, popupMenu.menu)

        for(i in albumNameList.indices) {
            // 갤러리 아이템 리스트 초기화
            popupMenu.menu.add(0, i, i, albumNameList[i])
        }

        popupMenu.setOnMenuItemClickListener {item ->
            binding.galleryButton.text = item.title.toString()
            imageAdapter.submitList(imagePickerViewModel.getFilterImageList(item.title.toString()))
            return@setOnMenuItemClickListener true
        }
    }
    private fun setupMenu() {
        (requireActivity() as SetProfileActivity).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.tolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d("ImagePickerFragment", "onMenuItemSelected : ${menuItem.itemId}")
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


    override fun onImageClick(imageItem: ImageItem) {
        activity?.supportFragmentManager?.setFragmentResult(
            URI_SELECTED,
            bundleOf("uri" to imageItem.uri, "bucket" to imageItem.bucket)
        )
        findNavController().navigateUp()
    }

    private fun setupAdapter() {
        imageAdapter = ImageAdapter(imagePickerViewModel)
        imageAdapter.setListener(this)
        binding.recyclerviewImage.adapter = imageAdapter
        CoroutineScope(Dispatchers.Main).launch {
            val itemList = imagePickerViewModel.getImageItemList()
            imageAdapter.submitList(itemList)
        }
    }
    private fun setupActionBar() {
        (activity as SetProfileActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (activity as SetProfileActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.finish)

        // 갤러리 선택용 버튼 텍스트 초기화
        binding.galleryButton.text = "전체"
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