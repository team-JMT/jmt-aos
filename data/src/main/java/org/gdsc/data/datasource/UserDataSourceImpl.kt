package org.gdsc.data.datasource

import okhttp3.MultipartBody
import org.gdsc.data.network.UserAPI
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import retrofit2.HttpException
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

    // TODO: Need to be Detailed
    override suspend fun checkDuplicatedNickname(nickname: String): Boolean {

        runCatching {
            userAPI.checkDuplicatedNickname(nickname).data
        }.onSuccess {
            return true
        }.onFailure { throwable ->
            if (throwable is HttpException) {
                when (throwable.code()) {
                    409 -> {
                        return false
                    }
                    500 -> {
                        return false
                    }
                }
            }
        }
        return true
    }
}