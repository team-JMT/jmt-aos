package org.gdsc.data.cursor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.gdsc.domain.model.Image
import org.gdsc.domain.model.MediaItem

class GalleryFolderCursorFactory: CursorFactory {
    override fun create(context: Context): Cursor? {
        val projection = getProjection()
        val mediaUri = getContentUri()
        val selectionArgs = null
        val selection = null
        val sortOrder = null

        return context.contentResolver.query(mediaUri, projection, selection, selectionArgs, sortOrder)
    }

    override fun getContentUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
    }

    override fun createMediaItem(cursor: Cursor): MediaItem {
        val bucketDisplayName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val bucketId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)

        return MediaItem(
            id = cursor.getLong(bucketId),
            uri = cursor.getString(bucketDisplayName)
        )
    }
}


/*
private fun getGalleryNames(context: Context): List<String> {
    val galleryNames = mutableListOf<String>()

    val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.BUCKET_ID
    )
    val cursor = context.contentResolver.query(uri, projection, null, null, null)

    cursor?.use { c ->
        val nameColumn: Int = c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (c.moveToNext()) {
            val name = c.getString(nameColumn)
            if (!galleryNames.contains(name)) {
                galleryNames.add(name)
            }
        }
    }

    return galleryNames
}
 */