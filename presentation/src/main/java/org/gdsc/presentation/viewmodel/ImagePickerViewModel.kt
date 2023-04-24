package org.gdsc.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.gdsc.domain.model.MediaItem
import org.gdsc.domain.usecase.GetGalleryUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"
    private val initAlbum = "전체"

    private lateinit var imageList: List<MediaItem>
    /*
    private var currentGalleryName = MutableLiveData<String>("123")

    val _images: LiveData<PagingData<MediaItem>> = currentGalleryName.switchMap { galleryName ->
        viewModelScope.launch {  }
        getGalleryUseCase().cachedIn(viewModelScope).asLiveData()
    }

     */
    private val _mediaListStateFlow = MutableStateFlow<PagingData<MediaItem>>(PagingData.empty())
    val mediaListStateFlow: StateFlow<PagingData<MediaItem>> = _mediaListStateFlow


    fun fetchMediaList() {
        viewModelScope.launch {
            try {
                val pagingDataFlow = getGalleryUseCase.invoke().cachedIn(viewModelScope)
                pagingDataFlow.collect {
                    _mediaListStateFlow.emit(it)
                }
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

/*
    fun setCurrentGalleryName(galleryName: String) {
        if (galleryName != currentGalleryName.value) {
            currentGalleryName.value = galleryName
        }
    }

 */



    /*
    private val albumList: List<String> by lazy {
        imageList.map { imageItem ->
            imageItem.bucket
        }.distinct()
    }

fun getImage() {
    viewModelScope.launch {
        val temp = getGalleryUseCase()
        val PAGE_SIZE = 20

        val imagesPagingDataFlow = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { getGalleryUseCase() }
        ).flow.cachedIn(viewModelScope)

        imagesFlow.value = imagesPagingDataFlow

    }
}

     */

    /*


    fun getInitAlbum():String {
        return initAlbum
    }

    suspend fun getImageItemList():List<MediaItem> {
        return withContext(Dispatchers.IO) {
            getGalleryUseCase()
                .runCatching {
                    imageList = this.map { filePath ->
                        val filePathSplit = filePath.split(File.separator)
                        MediaItem(Uri.fromFile(File(filePath)).toString(), filePathSplit[filePathSplit.size - 2])
                    }
                }
            return@withContext imageList
        }
    }



    fun getGalleryAlbum():List<String> {
        return listOf(initAlbum) + albumList
    }

    fun getFilterImageList(album: String):List<MediaItem> {
        return imageList.filter { if(album == initAlbum) true else it.bucket == album }
    }

 */
}