package org.gdsc.domain.usecase.user

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class UpdateSearchedKeywordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(keyword: String): List<String> {
        return userRepository.updateSearchedKeyword(keyword)
    }
}