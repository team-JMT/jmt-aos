package org.gdsc.data.datasource

import okhttp3.MultipartBody
import org.gdsc.domain.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo

interface UserDataSource {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse

    suspend fun postProfileImg(file: MultipartBody.Part): Response<String>

    suspend fun postDefaultProfileImg(): Response<String>

    suspend fun checkDuplicatedNickname(nickname: String): Boolean

    suspend fun getUserInfo(): UserInfo

    suspend fun getOtherUserInfo(id: Int): UserInfo

    suspend fun postUserLogout(refreshToken: String): String

    suspend fun postUserSignout(): String

    suspend fun getSearchedKeywords(): List<String>

    suspend fun updateSearchedKeyword(newKeyword: String): List<String>

    suspend fun deleteSearchedKeyword(targetKeyword: String): List<String>

    suspend fun initSearchedKeyword(): List<String>
}