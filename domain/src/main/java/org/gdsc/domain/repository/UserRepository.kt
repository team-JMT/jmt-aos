package org.gdsc.domain.repository

import okhttp3.MultipartBody
import org.gdsc.domain.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo

interface UserRepository {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse

    suspend fun postProfileImg(file: MultipartBody.Part): Response<String>

    suspend fun postDefaultProfileImg(): Response<String>

    suspend fun checkDuplicatedNickname(nickname: String): Boolean

    suspend fun getUserInfo(): UserInfo

    suspend fun getOtherUserInfo(id: Int): UserInfo

    suspend fun postUserLogout(refreshToken: String): String

    suspend fun postUserSignout(): String
}