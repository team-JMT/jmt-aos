package org.gdsc.domain.repository

import okhttp3.MultipartBody
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse

interface UserRepository {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse

    suspend fun postProfileImg(file: MultipartBody.Part): String
}