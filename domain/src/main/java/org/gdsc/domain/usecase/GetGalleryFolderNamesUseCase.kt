package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.MediaItem
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GetGalleryFolderNamesUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    suspend operator fun invoke(): List<String> {
        return galleryRepository.getGalleryFolderNames()
    }
}