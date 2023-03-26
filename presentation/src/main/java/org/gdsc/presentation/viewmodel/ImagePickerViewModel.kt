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

    val galleryName = MutableStateFlow(initAlbum)

    // 갤러리 이미지 Flow
    // 선택 앨범에 따라 리스트 내용 변경을 위한 fliter
    val imageItemListFlow: StateFlow<MutableList<ImageItem>> = _imageItemListFlow
        .combine(galleryName) { list, filter ->
        list.filter { if(galleryName.value == initAlbum) true else it.bucket.contains(filter) }.toMutableList() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, mutableListOf())



    suspend fun fetchImageItemList() {
        viewModelScope.launch {
            try {
                getGalleryUseCase()
                    .catch { e -> Log.d(TAG, "fetchImageItemList: ${e.message}") }
                    .collect { imageList ->
                        val folderList = ArrayList<String>()
                        val imageItemList = mutableListOf<ImageItem>()

                        // 이미지 추출 및 앨범명 추충
                        imageList.forEach {  filePath ->

                            val paths = filePath.split(File.separator)
                            val folder = paths[paths.size-2]

                            /*
                            // 현재는 getGalleryAlbum() 메서드를 통해 _imageItemListFlow 으로부터 앨범명 리스트를 추출
                            // Flow 이기에 데이터 변경에 대한 처리가 필요 없는 _imageItemListFlow로 추출하는게 효율적인지, folderList의 값을 사용하는 것이 효율적인지 확인이 필요.
                            // folderList의 값을 넣었을 때 데이터 변화에 유동적으로 대처 가능 한지 확인이 안 됨
                            // 앨범 중복 제거
                            if (!folderList.contains(folder)) {
                                folderList.add(folder)
                            }

                             */

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

    // 갤러리 이미지 통해 앨범명 리스트 추출
    fun getGalleryAlbum():List<String> {
        return listOf(initAlbum) + _imageItemListFlow.value.map { imageItem ->
            imageItem.bucket
        }.distinct()
    }

    companion object {
        const val initAlbum: String = "전체"
    }
}