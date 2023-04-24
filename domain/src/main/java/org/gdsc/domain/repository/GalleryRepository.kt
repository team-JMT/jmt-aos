package org.gdsc.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.MediaItem

interface GalleryRepository {
    suspend fun getGalleryImage(): Flow<PagingData<MediaItem>>
}