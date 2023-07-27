package org.gdsc.presentation.view.restaurantregistration

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.R
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.view.restaurantregistration.adapter.MultiImagePickerAdapter
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.utils.toPx
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

@AndroidEntryPoint
class MultiImagePickerFragment : Fragment(), GalleryImageClickListener {
    private lateinit var callback: OnBackPressedCallback

    private lateinit var popupMenu: PopupMenu

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val imagePickerViewModel: ImagePickerViewModel by viewModels()

    private lateinit var imageAdapter: MultiImagePickerAdapter

    private var albumFolderList: List<String> = listOf()

    private val imageList = mutableListOf<MediaItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        return binding.root
    }
    private fun setupPopUpMenu(view: View) {
        val albumNameList = (listOf(getString(R.string.album_all)) + albumFolderList).toTypedArray()

        popupMenu = PopupMenu(requireContext(), view)
        (requireActivity() as MainActivity).menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        for(i in albumNameList.indices) {
            // 갤러리 아이템 리스트 초기화
            popupMenu.menu.add(0, i, i, albumNameList[i])
        }

        popupMenu.setOnMenuItemClickListener {item ->
            // 앨범 선택 버튼 텍스트 변경

            binding.galleryButton.text = item.title.toString()
            imagePickerViewModel.album.value = item.title.toString()

            return@setOnMenuItemClickListener true
        }
    }
    private fun setMenu() {
        (requireActivity() as MainActivity).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.tolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // 뒤로가기 버튼
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

    private fun setView() {
        binding.multiImageInfoLayout.isVisible = true

        binding.multiImageSaveBtn.text = getString(R.string.select_text_counter_max_ten, imageList.size)

        binding.multiImageSaveBtn.setOnClickListener {
            setFragmentResult(
                "pickImages", bundleOf(
                    "imagesUri" to (imageList.map { it.uri }.toTypedArray()?: arrayOf())
                ))

            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenu()
        setAdapter()
        setView()

        setBaseToolbarVisible()
        setActionBar()

        // 앨범 선택용 버튼 (팝업 메뉴)
        binding.galleryButton.setOnClickListener { view ->
            setupPopUpMenu(view)
            popupMenu.show()
        }
    }

    // SignUpCompleteFragment로 선택 이미지와 갤러리명 넘기기
    override fun onImageClick(mediaItem: MediaItem) {
        if(imageList.contains(mediaItem)) {
            imageList.remove(mediaItem)
        } else {
            imageList.add(mediaItem)
        }
        binding.multiImageSaveBtn.text = getString(R.string.select_text_counter_max_ten, imageList.size)
    }

    private fun setAdapter() {

        imageAdapter = MultiImagePickerAdapter()
        imageAdapter.setListener(this)
        binding.recyclerviewImage.adapter = imageAdapter

        repeatWhenUiStarted {
            imagePickerViewModel.galleryFolderFlow.collect { galleryFolderNames ->
                // 앨범 선택용 버튼 텍스트 초기화
                albumFolderList = galleryFolderNames
            }
        }

        repeatWhenUiStarted {
            imagePickerViewModel.mediaListStateFlow.collect { mediaList ->
                imageAdapter.submitData(mediaList)
            }
        }

        repeatWhenUiStarted {
            imagePickerViewModel.album.collect {
                binding.galleryButton.text = it
            }
        }

        imagePickerViewModel.fetchGalleryFolderNames()
        imagePickerViewModel.fetchMediaList()

    }
    private fun setActionBar() {

        val toolbarView = binding.toolbar

        (requireActivity() as MainActivity).setSupportActionBar(toolbarView)
        requireNotNull((requireActivity() as MainActivity).supportActionBar).apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_arrow)
        }

        // for status bar size margin
        (toolbarView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(0, getStatusBarHeight(), 0, 0)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachBackPressedCallback()
    }

    // 뒤로가기 버튼 눌렀을 때 SignUpCompleteFragment로 이동
    private fun attachBackPressedCallback() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                findNavController().navigateUp()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    override fun onDetach() {
        super.onDetach()
        imagePickerViewModel.resetGallery()
        callback.remove()

    }

    private fun setBaseToolbarVisible() {
        (requireActivity() as MainActivity).changeToolbarVisible(false)
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun getStatusBarHeight(): Int {
        var result = 24.toPx
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}