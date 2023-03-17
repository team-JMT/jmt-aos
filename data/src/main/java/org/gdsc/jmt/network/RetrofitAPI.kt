package org.gdsc.jmt.network


import org.gdsc.jmt.model.JsonPlaceDTO
import org.gdsc.jmt.model.JsonPlaceDTO2
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAPI {
    @POST("api/v1/auth/google")
    fun sendUserGoogleToken(
        @Body jsonPlace: JsonPlaceDTO
    ): Call<JsonPlaceDTO>

    @POST("api/v1/auth/android/apple")
    fun sendUserAppleToken(
        @Body jsonPlace: JsonPlaceDTO2
    ): Call<JsonPlaceDTO2>

}