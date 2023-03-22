package org.gdsc.domain.usecase

import org.gdsc.domain.model.LoginResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class getGalleryImageUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(): ArrayList<String> {
        return loginRepository.getGalleryImage()
    }
}