package org.gdsc.domain.usecase.token

import org.gdsc.domain.repository.TokenRepository
import javax.inject.Inject

class PostRefreshTokenUseCase@Inject constructor(
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(refreshToken: String): Boolean {
        return tokenRepository.requestRefreshToken(refreshToken)
    }
}