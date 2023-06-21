package org.gdsc.data.datasource

import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse

interface UserDataSource {

    suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse
}