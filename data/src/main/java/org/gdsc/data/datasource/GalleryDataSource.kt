package org.gdsc.data.datasource

import kotlinx.coroutines.flow.Flow

interface GalleryDataSource {
    fun getGalleryImage(): List<String>
}