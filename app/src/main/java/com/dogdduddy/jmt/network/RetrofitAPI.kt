package com.dogdduddy.jmt.network

import com.dogdduddy.jmt.model.JsonPlaceDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitAPI {
    @POST("api/v1/auth/google")
    fun sendUserGoogleToken(
        @Body jsonPlace: JsonPlaceDTO
    ): retrofit2.Call<JsonPlaceDTO>

    @POST("/posts")
    fun callPost(
        @Body token: String
    ) : Call<JSONObject>
}