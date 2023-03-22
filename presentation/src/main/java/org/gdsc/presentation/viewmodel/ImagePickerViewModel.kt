package org.gdsc.presentation.viewmodel

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.usecase.GetGalleryUseCase
import org.gdsc.presentation.data.ImageItem
import java.io.File
import javax.inject.Inject

private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
private const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"
    val imageItemList = MutableLiveData<MutableList<ImageItem>>(mutableListOf())

    fun fetchImageItemList() {

        val folderList = ArrayList<String>()

        val filesPath = getGalleryUseCase()

        filesPath.forEach {filePath ->
            val paths = filePath.split(File.separator)
            val folder = paths[paths.size-2]


            if (!folderList.contains(folder)) {
                folderList.add(folder)
            }

            imageItemList.value!!.add(
                ImageItem(Uri.fromFile(File(filePath)).toString(),folder)
            )
        }

        Log.d("DataTest", "folders : ${folderList.size}")
        Log.d("DataTest", "folders : ${folderList}")

    }
}