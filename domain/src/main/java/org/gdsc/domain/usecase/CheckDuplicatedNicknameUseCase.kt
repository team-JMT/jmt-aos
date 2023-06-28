package org.gdsc.domain.usecase

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class CheckDuplicatedNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
){

    suspend operator fun invoke(nickname: String): Boolean {
        return userRepository.checkDuplicatedNickname(nickname)
    }
}