package org.gdsc.data.datasource

import okhttp3.MultipartBody
import org.gdsc.data.network.UserAPI
import org.gdsc.domain.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.model.request.LogoutRequest
import retrofit2.HttpException
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userAPI: UserAPI
) : UserDataSource {
    override suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        return userAPI.postNickname(nicknameRequest).data
    }

    override suspend fun postProfileImg(file: MultipartBody.Part): Response<String> {
        return userAPI.postProfileImg(file)
    }

    override suspend fun postDefaultProfileImg(): Response<String> {
        return userAPI.postDefaultProfileImg()
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

    override suspend fun getUserInfo(): UserInfo {
        kotlin.runCatching {
            userAPI.getUserInfo().data
        }.onSuccess {
            return it
        }.onFailure {
            throw it
        }
        return UserInfo()
    }

    override suspend fun postUserLogout(refreshToken: String): String {
        return userAPI.postUserLogOut(LogoutRequest(refreshToken)).code
    }

    override suspend fun postUserSignout(): String {
        return userAPI.postUserSignOut().code
    }
}