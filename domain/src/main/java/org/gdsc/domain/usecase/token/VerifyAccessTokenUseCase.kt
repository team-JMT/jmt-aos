package org.gdsc.domain.usecase.token

import org.gdsc.domain.DateManager
import javax.inject.Inject

class VerifyAccessTokenUseCase @Inject constructor(
    private val getAccessTokenExpiresInUseCase: GetAccessTokenExpiresInUseCase,
) {
    suspend operator fun invoke(): Boolean {
        return DateManager.isValidAccessToken(getAccessTokenExpiresInUseCase())
    }
}