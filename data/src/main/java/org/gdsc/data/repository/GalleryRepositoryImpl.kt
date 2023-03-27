package org.gdsc.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.gdsc.data.datasource.GalleryDataSource
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource
): GalleryRepository {
    override fun getGalleryImage(): List<String> {
        return galleryDataSource.getGalleryImage()

    }
}