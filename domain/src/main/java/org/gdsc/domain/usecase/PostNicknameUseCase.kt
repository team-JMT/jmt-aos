package org.gdsc.domain.usecase

import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class PostNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String) =
        userRepository.postNickname(NicknameRequest(nickname))
}