package org.gdsc.data.network


import org.gdsc.data.model.Response
import org.gdsc.domain.model.request.GoogleLoginRequest
import org.gdsc.domain.model.request.AppleLoginRequest
import org.gdsc.domain.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.POST

interface LoginAPI {
    @POST("api/v1/auth/google")
    suspend fun postUserGoogleToken(
        @Body request: GoogleLoginRequest
    ): Response<TokenResponse>

    @POST("api/v1/auth/android/apple")
    suspend fun postUserAppleToken(
        @Body request: AppleLoginRequest
    ): Response<TokenResponse>

}