package org.gdsc.data.datasource

import android.content.Context
import android.database.Cursor
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.qualifiers.ApplicationContext
import org.gdsc.data.cursor.CursorFactory
import org.gdsc.domain.model.MediaItem
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cursorFactory: CursorFactory
): GalleryDataSource, PagingSource<Int, MediaItem>() {

    private var cursor = cursorFactory.create(context)

    override suspend fun getGalleryImage(): List<MediaItem> {
        cursor?.let {
            return getMediaList(it, it.count)
        } ?: run {
            return listOf()
        }
    }

    override suspend fun getGalleryFolderName(): List<String> {
        /*
        cursor?.use { c ->
            cursor?.let {
                val galleryNames = mutableListOf<String>()
                val nameColumn: Int = c.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                while (c.moveToNext()) {
                    val name = c.getString(nameColumn)
                    if (!galleryNames.contains(name)) {
                        galleryNames.add(name)
                    }
                }
                return galleryNames
            }
            while (c.moveToNext()) {
                val name = c.getString(nameColumn)
                if (!galleryNames.contains(name)) {
                    galleryNames.add(name)
                }
            }
        }


         */
        return listOf("")
    }

    override fun create(): PagingSource<Int, MediaItem> {
        return this
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        return try {
            // 현재 페이지의 첫 인덱스 값을 가져옵니다.
            val currentPage = params.key ?: 0

            var data: ArrayList<MediaItem> = ArrayList()

            cursor?.let {
                data = getMediaList(it, params.loadSize)
            }
            val prevPage = if (currentPage == 0) null else currentPage - 1
            val nextPage = if (data.size < params.loadSize) null else currentPage + 1

            LoadResult.Page(data, prevPage, nextPage)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun getMediaList(cursor: Cursor?, loadSize: Int): ArrayList<MediaItem> {
        val mediaList = ArrayList<MediaItem>()
        cursor?.let {
                for (i in 0 until loadSize) {
                    if (!it.moveToNext()) {
                        break
                    }
                    mediaList.add(cursorFactory.createMediaItem(it))
                }
            return mediaList
        } ?: run {
            return mediaList
        }
    }
}