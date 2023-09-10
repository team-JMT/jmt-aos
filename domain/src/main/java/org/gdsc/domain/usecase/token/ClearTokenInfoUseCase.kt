package org.gdsc.domain.usecase.token

import org.gdsc.domain.repository.TokenRepository
import javax.inject.Inject

class ClearTokenInfoUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() {
        tokenRepository.clearTokenInfo()
    }
}