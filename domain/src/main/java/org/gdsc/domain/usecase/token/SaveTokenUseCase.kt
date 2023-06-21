package org.gdsc.domain.usecase.token

import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.TokenManager
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val tokenManager: TokenManager,
){

    suspend operator fun invoke(
        response: TokenResponse
    ) {
        tokenManager.saveTokenInfo(response)

    }
}