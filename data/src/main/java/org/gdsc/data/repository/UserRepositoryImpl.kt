package org.gdsc.data.repository

import org.gdsc.data.datasource.UserDataSource
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        return userDataSource.postNickname(nicknameRequest)
    }

}