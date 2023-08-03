package org.gdsc.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.BaseActivity
import org.gdsc.presentation.R
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.adapter.SingleImagePickerAdapter
import org.gdsc.presentation.databinding.FragmentImagePickerBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.viewmodel.ImagePickerViewModel


@AndroidEntryPoint
class ImagePickerFragment : Fragment(), GalleryImageClickListener {

    private lateinit var popupMenu:PopupMenu

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val imagePickerViewModel: ImagePickerViewModel by viewModels()

    private lateinit var imageAdapter: SingleImagePickerAdapter

    private var albumFolderList: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)

        return binding.root
    }
    private fun setupPopUpMenu(view: View) {
        val albumNameList = (listOf("전체") + albumFolderList).toTypedArray()

        popupMenu = PopupMenu(requireContext(), view)
        requireActivity().menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

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

    private fun setView() {
        binding.multiImageInfoLayout.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBar()
        setAdapter()
        setView()
        setBaseToolbarVisible()

        // 앨범 선택용 버튼 (팝업 메뉴)
        binding.galleryButton.setOnClickListener { view ->
            setupPopUpMenu(view)
            popupMenu.show()
        }

    }

    // SignUpCompleteFragment로 선택 이미지와 갤러리명 넘기기
    override fun onImageClick(mediaItem: MediaItem) {
        val imageUri = mediaItem.uri

        setFragmentResult("pickImage", Bundle().apply {
            putString("imageUri", imageUri)
        })

        findNavController().navigateUp()
    }

    private fun setAdapter() {

        imageAdapter = SingleImagePickerAdapter(imagePickerViewModel)
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
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        requireNotNull((requireActivity() as AppCompatActivity).supportActionBar).apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_arrow)
        }
    }

    private fun setBaseToolbarVisible() {
        if(requireActivity() is MainActivity)
            (requireActivity() as MainActivity).changeToolbarVisible(false)
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

    }
}