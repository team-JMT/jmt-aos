package org.gdsc.data.network

import org.gdsc.data.model.Response
import org.gdsc.domain.model.request.RefreshTokenRequest
import org.gdsc.domain.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenAPI {
    @POST("api/v1/token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<TokenResponse>
}