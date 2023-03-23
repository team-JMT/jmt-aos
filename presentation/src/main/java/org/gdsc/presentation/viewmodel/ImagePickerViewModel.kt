package org.gdsc.presentation.viewmodel

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdsc.domain.usecase.GetGalleryUseCase
import org.gdsc.presentation.data.ImageItem
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"

    private val _imageItemListFlow = MutableStateFlow<MutableList<ImageItem>>(mutableListOf<ImageItem>())

    val galleryName = MutableStateFlow("전체")

    var filter: String
        get() = galleryName.value
        set(value) {
            galleryName.value = value
        }


    val imageItemListFlow: StateFlow<MutableList<ImageItem>> = _imageItemListFlow
        .combine(galleryName) { list, filter ->
        list.filter { if(galleryName.value=="전체") true else it.bucket.contains(filter) }.toMutableList() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, mutableListOf())



    suspend fun fetchImageItemList() {
        viewModelScope.launch {
            try {
                getGalleryUseCase()
                    .catch { e -> Log.d(TAG, "fetchImageItemList: ${e.message}") }
                    .collect { imageList ->
                        val folderList = ArrayList<String>()
                        val imageItemList = mutableListOf<ImageItem>()

                        imageList.forEach {  filePath ->

                            val paths = filePath.split(File.separator)
                            val folder = paths[paths.size-2]

                            if (!folderList.contains(folder)) {
                                folderList.add(folder)
                            }

                            imageItemList.add(
                                ImageItem(Uri.fromFile(File(filePath)).toString(),folder)
                            )
                        }
                        _imageItemListFlow.value = imageItemList
                    }
            } catch (e: Exception) {
                Log.d(TAG, "fetchImageItemList: ${e.message}")
            }
        }
    }

    fun getGalleryAlbum():List<String> {
        return listOf("전체") + _imageItemListFlow.value.map { imageItem ->
            imageItem.bucket
        }.distinct()
    }
}