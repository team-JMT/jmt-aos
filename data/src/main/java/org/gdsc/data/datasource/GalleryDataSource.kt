package org.gdsc.data.datasource


import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.MediaItem

interface GalleryDataSource {
    fun getGalleryImage(album: String): PagingSource<Int, MediaItem>

    suspend fun getGalleryFolderName(): List<String>

    fun reset()
}