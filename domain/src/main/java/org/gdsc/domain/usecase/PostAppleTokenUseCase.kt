package org.gdsc.domain.usecase

import org.gdsc.domain.model.LoginResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class PostAppleTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String, clientId: String): LoginResponse {
        return loginRepository.postAppleToken(email, clientId)
    }
}
