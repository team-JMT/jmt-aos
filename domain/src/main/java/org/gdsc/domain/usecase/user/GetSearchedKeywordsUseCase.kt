package org.gdsc.domain.usecase.user

import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class GetSearchedKeywordsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<String> {
        return userRepository.getSearchedKeywords()
    }
}