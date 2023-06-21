package org.gdsc.domain.usecase

import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class PostAppleTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(email: String, clientId: String): TokenResponse {
        return loginRepository.postAppleToken(email, clientId)
    }
}
