package org.gdsc.data.datasource

import org.gdsc.data.network.UserAPI
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userAPI: UserAPI
) : UserDataSource {
    override suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        return userAPI.postNickname(nicknameRequest).data
    }
}