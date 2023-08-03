package org.gdsc.domain.usecase

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class PostUserSignoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): String {
        return userRepository.postUserSignout()
    }
}
