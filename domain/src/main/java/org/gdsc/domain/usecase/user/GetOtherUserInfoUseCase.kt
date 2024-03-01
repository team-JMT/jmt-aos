package org.gdsc.domain.usecase.user

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class GetOtherUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(id: Int) = userRepository.getOtherUserInfo(id)
}