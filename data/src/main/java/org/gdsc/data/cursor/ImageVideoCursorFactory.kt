package org.gdsc.data.cursor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.gdsc.domain.model.Image
import org.gdsc.domain.model.MediaItem

class ImageVideoCursorFactory: CursorFactory {
    override fun create(context: Context, album: String): Cursor? {
        val projection = getProjection()
        val mediaUri = getContentUri()
        val selection = "" +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                " OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"

        val selectionArgs = arrayOf(
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}",
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
        )

        val sortOrder = String.format("%s %s", MediaStore.MediaColumns.DATE_ADDED, "desc")

        return context.contentResolver.query(
            mediaUri, projection,
            selection, selectionArgs, sortOrder
        )
    }

    override fun getContentUri(): Uri {
        return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    }

    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DURATION,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Files.FileColumns.ORIENTATION
        )
    }

    override fun createMediaItem(cursor: Cursor): MediaItem {
        val id = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
        val dateAdded = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
        val bucketId = cursor.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_ID)
        val fileSize = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
        val mimeType = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
        val orientation = cursor.getColumnIndex(MediaStore.Files.FileColumns.ORIENTATION)
        val url = ContentUris.withAppendedId(getContentUri(),cursor.getLong(id))
        val albumName = cursor.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
        val albumTitle = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
        val duration = cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)

        val image = Image(
            id = cursor.getLong(id),
            bucket = cursor.getString(bucketId),
            dateAdded = cursor.getLong(dateAdded),
            fileSize = cursor.getLong(fileSize),
            mimeType = cursor.getString(mimeType),
            orientation = cursor.getInt(orientation),
            albumName = cursor.getString(albumName) ?: cursor.getString(albumTitle),
            uri = url.toString()
        )

        return MediaItem(
            id = image.id,
            uri = image.uri,
            albumName = image.albumName
        )
    }
}