package org.gdsc.domain.usecase

import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class GetGalleryUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    suspend operator fun invoke(): List<String> {
        return galleryRepository.getGalleryImage()
    }
}