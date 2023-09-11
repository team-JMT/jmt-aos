package org.gdsc.data.network


import org.gdsc.domain.model.Response
import org.gdsc.domain.model.request.GoogleLoginRequest
import org.gdsc.domain.model.request.AppleLoginRequest
import org.gdsc.domain.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("api/v1/auth/google")
    suspend fun postUserGoogleToken(
        @Body request: GoogleLoginRequest
    ): retrofit2.Response<Response<TokenResponse>>

    @POST("api/v1/auth/android/apple")
    suspend fun postUserAppleToken(
        @Body request: AppleLoginRequest
    ): Response<TokenResponse>

}