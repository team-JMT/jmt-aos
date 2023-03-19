package org.gdsc.data.network


import org.gdsc.domain.model.GoogleLoginRequest
import org.gdsc.domain.model.AppleLoginRequest
import org.gdsc.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("api/v1/auth/google")
    suspend fun postUserGoogleToken(
        @Body request: GoogleLoginRequest
    ): LoginResponse

    @POST("api/v1/auth/android/apple")
    suspend fun postUserAppleToken(
        @Body request: AppleLoginRequest
    ): LoginResponse

}