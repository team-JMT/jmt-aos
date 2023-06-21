package org.gdsc.domain.usecase.token

import org.gdsc.domain.repository.TokenManager
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {

    suspend operator fun invoke(): String {
        return tokenManager.getAccessToken()
    }
}