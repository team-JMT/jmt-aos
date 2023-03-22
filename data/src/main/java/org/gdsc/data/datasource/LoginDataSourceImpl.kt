package org.gdsc.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.model.AppleLoginRequest
import org.gdsc.domain.model.GoogleLoginRequest
import org.gdsc.domain.model.LoginResponse
import java.io.File
import javax.inject.Inject


private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
private const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI,
    @ApplicationContext private val context: Context
) : LoginDataSource {
    override suspend fun postGoogleToken(token: String): LoginResponse {
        return loginAPI.postUserGoogleToken(GoogleLoginRequest(token))
    }

    override suspend fun postAppleToken(email: String, clientId: String): LoginResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId))
    }

    @SuppressLint("Range")
    override fun getGalleryImage(): ArrayList<String> {
        val imageItemList:ArrayList<String> = ArrayList()
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

        cursor?.let {
            while(cursor.moveToNext()) {
                val filePath = cursor.getString(cursor.getColumnIndex(INDEX_MEDIA_URI))
                imageItemList.add(filePath)
            }
        }

        cursor?.close()

        return imageItemList
    }
}