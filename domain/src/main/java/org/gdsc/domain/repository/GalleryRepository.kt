package org.gdsc.domain.repository

import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getGalleryImage(): Flow<MutableList<String>>
}