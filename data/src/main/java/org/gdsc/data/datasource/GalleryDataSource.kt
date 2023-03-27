package org.gdsc.data.datasource

interface GalleryDataSource {
    suspend fun getGalleryImage(): List<String>
}