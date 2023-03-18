package org.gdsc.data.network


import org.gdsc.data.model.GoogleLoginRequest
import org.gdsc.data.model.AppleLoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("api/v1/auth/google")
    fun sendUserGoogleToken(
        @Body request: GoogleLoginRequest
    ): Call<GoogleLoginRequest>

    @POST("api/v1/auth/android/apple")
    fun sendUserAppleToken(
        @Body request: AppleLoginRequest
    ): Call<AppleLoginRequest>

}