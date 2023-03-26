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
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
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
import org.gdsc.presentation.login.SignUpCompleteFragment.Companion.URI_SELECTED
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.viewmodel.ImagePickerViewModel


@AndroidEntryPoint
class ImagePickerFragment : Fragment(), GalleryImageClickListener {
    private lateinit var callback: OnBackPressedCallback

    private lateinit var popupMenu:PopupMenu

    private val directions = ImagePickerFragmentDirections.actionImagepickerFragmentToSignUpCompleteFragment()

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

        // 앨범 선택용 버튼 (팝업 메뉴)
        binding.galleryButton.setOnClickListener {view ->
            setupPopUpMenu(view)
            popupMenu.show()
        }

        return binding.root
    }
    private fun setupPopUpMenu(view: View) {
        val albumNameList = imagePickerViewModel.getGalleryAlbum().toTypedArray()

        popupMenu = PopupMenu(requireContext(), view)
        (requireActivity() as LoginActivity).menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        for(i in albumNameList.indices) {
            // 갤러리 아이템 리스트 초기화
            popupMenu.menu.add(0, i, i, albumNameList[i])
        }

        popupMenu.setOnMenuItemClickListener {item ->
            // 앨범 선택 버튼 텍스트 변경
            imagePickerViewModel.galleryName.value = item.title.toString()
            binding.galleryButton.text = item.title.toString()
            return@setOnMenuItemClickListener true
        }
    }
    private fun setupMenu() {
        (requireActivity() as LoginActivity).addMenuProvider(object: MenuProvider {
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

    // 갤러리 속 이미지 불러오기 및 Flow 구독
    private fun initGallery(adapter: ImageAdapter) {
        viewLifecycleOwner.repeatWhenUiStarted {
            imagePickerViewModel.imageItemListFlow.collect {
                adapter.submitList(it)
            }
        }
    }

    // SignUpCompleteFragment로 선택 이미지와 갤러리명 넘기기
    override fun onImageClick(imageItem: ImageItem) {
        val imageUri = imageItem.uri
        val navigation = ImagePickerFragmentDirections.actionImagepickerFragmentToSignUpCompleteFragment(imageUri)
        findNavController().navigate(navigation)
    }

    private fun setupAdapter() {
        val adapter = ImageAdapter(imagePickerViewModel)
        adapter.setListener(this)
        binding.recyclerviewImage.adapter = adapter
        initGallery(adapter)
    }
    private fun setupActionBar() {
        (requireActivity() as LoginActivity).setSupportActionBar(binding.toolbar)
        val actionBar = (requireActivity() as LoginActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.finish)

        // 앨범 선택용 버튼 텍스트 초기화
        binding.galleryButton.text = imagePickerViewModel.galleryName.value
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachBackPressedCallback()
    }

    // 뒤로가기 버튼 눌렀을 때 SignUpCompleteFragment로 이동
    private fun attachBackPressedCallback() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(directions)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}