package org.gdsc.domain.repository

import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse

interface UserRepository {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse
}