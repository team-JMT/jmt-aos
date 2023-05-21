package org.gdsc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.data.datasource.GalleryDataSource
import org.gdsc.domain.model.MediaItem
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
    override suspend fun getGalleryImage(album:String): Flow<PagingData<MediaItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 18,
                enablePlaceholders = false
            )) {
            galleryDataSource.getGalleryImage(album)
        }.flow
    }

    override suspend fun getGalleryFolderNames(): List<String> {
        return galleryDataSource.getGalleryFolderName()
    }

    override fun resetGalleryImage() {
        galleryDataSource.reset()
    }
}