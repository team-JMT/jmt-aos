package org.gdsc.domain.usecase

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class PostUserLogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(refreshToken: String): String {
        return userRepository.postUserLogout(refreshToken)
    }
}