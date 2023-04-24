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
    override suspend fun getGalleryImage(): Flow<PagingData<MediaItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            )) {
            galleryDataSource.create()
        }.flow

    }
}