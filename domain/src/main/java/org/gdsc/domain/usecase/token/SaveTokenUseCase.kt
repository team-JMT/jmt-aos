package org.gdsc.domain.usecase.token

import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.TokenRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
){

    suspend operator fun invoke(
        response: TokenResponse
    ) {
        tokenRepository.saveTokenInfo(response)

    }
}