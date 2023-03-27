package org.gdsc.data.repository

import org.gdsc.data.datasource.GalleryDataSource
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
    override suspend fun getGalleryImage(): List<String> {
        return galleryDataSource.getGalleryImage()

    }
}