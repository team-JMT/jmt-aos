package org.gdsc.domain.usecase.user

import okhttp3.MultipartBody
import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class PostProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part) = userRepository.postProfileImg(file)
}