package org.gdsc.domain.usecase

import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostSignUpWithGoogleTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(token: String): Result<TokenResponse> {
        return loginRepository.postSignUpWithGoogleToken(token)
    }
}
