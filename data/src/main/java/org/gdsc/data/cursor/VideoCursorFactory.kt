package org.gdsc.data.cursor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.gdsc.domain.model.Image
import org.gdsc.domain.model.MediaItem
import org.gdsc.domain.model.Video

class VideoCursorFactory: CursorFactory {
    override fun create(context: Context, album: String): Cursor? {
        val projection = getProjection()
        val mediaUri = getContentUri()
        val selectionArgs = null
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Video.Media.SIZE + " > 0"
            else null
        val sortOrder = String.format("%s %s", MediaStore.Video.Media.DATE_ADDED, "desc")

        return context.contentResolver.query(mediaUri, projection, selection, selectionArgs, sortOrder)
    }

    override fun getContentUri(): Uri {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Video.Media.ORIENTATION
        )
    }

    override fun createMediaItem(cursor: Cursor): MediaItem {
        val id = cursor.getColumnIndex(MediaStore.Video.Media._ID)
        val dateAdded = cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
        val bucketId = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
        val fileSize = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
        val mimeType = cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)
        val orientation = cursor.getColumnIndex(MediaStore.Video.Media.ORIENTATION)
        val albumName = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        val albumTitle = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
        val url = ContentUris.withAppendedId(getContentUri(),cursor.getLong(id))

        val video = Video(
            id = cursor.getLong(id),
            bucket = cursor.getString(bucketId),
            dateAdded = cursor.getLong(dateAdded),
            fileSize = cursor.getLong(fileSize),
            mimeType = cursor.getString(mimeType),
            orientation = cursor.getInt(orientation),
            duration = cursor.getLong(orientation),
            albumName = cursor.getString(albumName) ?: cursor.getString(albumTitle),
            uri = url.toString()
        )

        return MediaItem(
            id = video.id,
            uri = video.uri,
            albumName = video.albumName
        )
    }
}