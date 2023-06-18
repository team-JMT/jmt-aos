package org.gdsc.data.datasource

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.gdsc.data.cursor.CursorFactory
import org.gdsc.data.cursor.ImageCursorFactory
import org.gdsc.domain.model.MediaItem
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cursorFactory: ImageCursorFactory
): GalleryDataSource {

    private var cursor: Cursor? = null

    override fun reset() {
        cursor?.close()
    }

    override suspend fun getGalleryFolderName(): List<String> {

        cursor = cursorFactory.create(context, "")

        val galleryNames = ArrayList<String>()

        cursor?.use { c ->
            while (c.moveToNext()) {
                val item = cursorFactory.createMediaItem(c)
                if(!galleryNames.contains(item.albumName)) {
                    galleryNames.add(item.albumName)
                }
            }
        }

        cursor?.close()
        return galleryNames
    }


    private fun getMediaList(cursor: Cursor?, loadSize: Int): ArrayList<MediaItem> {
        val mediaList = ArrayList<MediaItem>()
        cursor?.let { c ->
            for (i in 0 until loadSize) {
                if (!c.moveToNext()) {
                    break
                }
                mediaList.add(cursorFactory.createMediaItem(c))
            }
            return mediaList
        } ?: run {
            return mediaList
        }
    }

    override fun getGalleryImage(album: String): PagingSource<Int, MediaItem> {
        cursor = cursorFactory.create(context, album)
        return object : PagingSource<Int, MediaItem>() {

            override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
                return try {

                    val currentPage = params.key ?: 0
                    val data = ArrayList<MediaItem>()

                    cursor?.let { c ->
                        data.addAll(getMediaList(c, params.loadSize))
                    }

                    val prevPage = if (currentPage == 0) null else currentPage - 1
                    val nextPage = if (data.size < params.loadSize) null else currentPage + 1
                    LoadResult.Page(data, prevPage, nextPage)
                } catch (e: Exception) {
                    cursor?.close()
                    LoadResult.Error(e)
                }
            }

        }
    }
}