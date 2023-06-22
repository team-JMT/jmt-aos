package org.gdsc.data.datasource

import okhttp3.MultipartBody
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

    override suspend fun postProfileImg(file: MultipartBody.Part): String {
        return userAPI.postProfileImg(file).data
    }
}