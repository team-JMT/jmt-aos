package org.gdsc.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.gdsc.domain.model.ImageItem
import org.gdsc.domain.usecase.GetGalleryUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"
    private val initAlbum = "전체"

    private lateinit var imageList: List<ImageItem>

    private val albumList: List<String> by lazy {
        imageList.map { imageItem ->
            imageItem.bucket
        }.distinct()
    }

    fun getInitAlbum():String {
        return initAlbum
    }

    suspend fun getImageItemList():List<ImageItem> {
        return withContext(Dispatchers.IO) {
            getGalleryUseCase()
                .runCatching {
                    imageList = this.map { filePath ->
                        val filePathSplit = filePath.split(File.separator)
                        ImageItem(Uri.fromFile(File(filePath)).toString(), filePathSplit[filePathSplit.size - 2])
                    }
                }
            return@withContext imageList
        }
    }

    fun getGalleryAlbum():List<String> {
        return listOf(initAlbum) + albumList
    }

    fun getFilterImageList(album: String):List<ImageItem> {
        return imageList.filter { if(album == initAlbum) true else it.bucket == album }
    }
}