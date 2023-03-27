package org.gdsc.domain.repository

interface GalleryRepository {
    suspend fun getGalleryImage(): List<String>
}