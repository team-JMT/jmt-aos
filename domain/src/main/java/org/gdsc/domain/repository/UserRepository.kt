package org.gdsc.domain.repository

import okhttp3.MultipartBody
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo

interface UserRepository {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse

    suspend fun postProfileImg(file: MultipartBody.Part): String

    suspend fun postDefaultProfileImg(): String

    suspend fun checkDuplicatedNickname(nickname: String): Boolean

    suspend fun getUserInfo(): UserInfo

    suspend fun postUserLogout(refreshToken: String): String
}