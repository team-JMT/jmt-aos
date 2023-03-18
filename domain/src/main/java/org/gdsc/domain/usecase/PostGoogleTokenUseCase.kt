package org.gdsc.domain.usecase

import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostGoogleTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(token: String) {
        loginRepository.postGoogleToken(token)
    }
}
