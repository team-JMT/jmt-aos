package org.gdsc.domain.usecase

import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Inject

class ResetGalleryUseCase @Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    operator fun invoke() {
        return galleryRepository.resetGalleryImage()
    }
}