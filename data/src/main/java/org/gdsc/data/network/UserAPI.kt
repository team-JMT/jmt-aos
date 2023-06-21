package org.gdsc.data.network

import org.gdsc.data.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("api/v1/user/nickname")
    suspend fun postNickname(@Body nicknameRequest: NicknameRequest): Response<NicknameResponse>
}