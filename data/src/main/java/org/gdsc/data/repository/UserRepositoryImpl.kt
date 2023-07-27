package org.gdsc.data.repository

import okhttp3.MultipartBody
import org.gdsc.data.datasource.UserDataSource
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        return userDataSource.postNickname(nicknameRequest)
    }

    override suspend fun postProfileImg(file: MultipartBody.Part): String {
        return userDataSource.postProfileImg(file)
    }

    override suspend fun postDefaultProfileImg(): String {
        return userDataSource.postDefaultProfileImg()
    }

    override suspend fun checkDuplicatedNickname(nickname: String): Boolean {
        return userDataSource.checkDuplicatedNickname(nickname)
    }

    override suspend fun getUserInfo(): UserInfo {
        return userDataSource.getUserInfo()
    }

}