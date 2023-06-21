package org.gdsc.domain.usecase.token

import org.gdsc.domain.repository.TokenManager
import javax.inject.Inject

class GetAccessTokenExpiresInUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {

    suspend operator fun invoke(): Long {
        return tokenManager.getAccessTokenExpiresIn()
    }
}