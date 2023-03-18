package org.gdsc.domain.usecase

import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class PostAppleTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, clientId: String) {
        loginRepository.postAppleToken(email, clientId)
    }
}
