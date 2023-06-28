package org.gdsc.data.datasource

import okhttp3.MultipartBody
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse

interface UserDataSource {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse

    suspend fun postProfileImg(file: MultipartBody.Part): String

    suspend fun checkDuplicatedNickname(nickname: String): Boolean
}