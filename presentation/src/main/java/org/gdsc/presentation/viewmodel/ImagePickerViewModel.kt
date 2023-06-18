package org.gdsc.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.gdsc.domain.model.MediaItem
import org.gdsc.domain.usecase.GetGalleryFolderNamesUseCase
import org.gdsc.domain.usecase.GetGalleryUseCase
import org.gdsc.domain.usecase.ResetGalleryUseCase
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase,
    private val initGalleryUseCase: ResetGalleryUseCase,
    private val getGalleryFolderNamesUseCase: GetGalleryFolderNamesUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"

    val album = MutableStateFlow("전체")

    private val _galleryFolderFlow = MutableStateFlow(listOf<String>(album.toString()))
    val galleryFolderFlow: StateFlow<List<String>> = _galleryFolderFlow

    private val _mediaListStateFlow = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
    val mediaListStateFlow: StateFlow<PagingData<MediaItem>> = _mediaListStateFlow


    fun fetchMediaList() {
        viewModelScope.launch {
            try {
                album.flatMapLatest {
                    getGalleryUseCase(it)
                }.cachedIn(viewModelScope)
                    .collectLatest {
                        _mediaListStateFlow.emit(it)
                    }
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun resetGallery() {
        initGalleryUseCase()
    }

    fun fetchGalleryFolderNames() {
        viewModelScope.launch {
            getGalleryFolderNamesUseCase().runCatching {
                _galleryFolderFlow.emit(this)
            }.onFailure {
                Log.e(TAG, "fetchGalleryFolderNames: $it")
            }
        }
    }
}