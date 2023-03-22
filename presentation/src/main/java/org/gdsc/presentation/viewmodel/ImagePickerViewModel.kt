package org.gdsc.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.usecase.getGalleryImageUseCase
import org.gdsc.presentation.data.ImageItem
import java.io.File
import javax.inject.Inject

private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
private const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getGalleryUseCase: getGalleryImageUseCase
): ViewModel() {
    private val TAG = "ImagePickerViewModel"
    val imageItemList = MutableLiveData<MutableList<ImageItem>>(mutableListOf())

    fun fetchImageItemList(context: Context) {
        /*
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            INDEX_MEDIA_ID,
            INDEX_MEDIA_URI,
            INDEX_ALBUM_NAME,
            INDEX_DATE_ADDED
        )

        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null
        val sortOrder = "$INDEX_DATE_ADDED DESC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        val folderList = ArrayList<String>()

        cursor?.let {
            while(cursor.moveToNext()) {
                val mediaPath = cursor.getString(cursor.getColumnIndex(INDEX_MEDIA_URI))

                // 갤러리 마지막 경로 가져오기
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val filePath = cursor.getString(columnIndex)
                val paths = filePath.split(File.separator)
                val folder = paths[paths.size-2]

                if (!folderList.contains(folder)) {
                    folderList.add(folder)
                }
                Log.d(TAG, "filePAth: $filePath")

                // 이미지 리스트에 추가
                imageItemList.value!!.add(
                    ImageItem(Uri.fromFile(File(mediaPath)),folder)
                )
            }
        }

        cursor?.close()

         */

        val folderList = ArrayList<String>()

        val filesPath = getGalleryUseCase()

        Log.d("DataTest", "filesPath : ${filesPath}")

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