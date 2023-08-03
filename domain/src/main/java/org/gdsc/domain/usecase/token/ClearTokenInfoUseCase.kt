package org.gdsc.domain.usecase.token

import org.gdsc.domain.repository.TokenManager
import javax.inject.Inject

class ClearTokenInfoUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() {
        tokenManager.clearTokenInfo()
    }
}