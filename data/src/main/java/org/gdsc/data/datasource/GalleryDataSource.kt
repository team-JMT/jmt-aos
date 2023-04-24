package org.gdsc.data.datasource


import androidx.paging.PagingSource
import org.gdsc.domain.model.MediaItem

interface GalleryDataSource {
    suspend fun getGalleryImage(): List<MediaItem>

    suspend fun getGalleryFolderName(): List<String>

    fun create(): PagingSource<Int, MediaItem>
}