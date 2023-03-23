package org.gdsc.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GetGalleryUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    operator fun invoke(): Flow<MutableList<String>> {
        return galleryRepository.getGalleryImage()
    }
}