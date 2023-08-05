package org.gdsc.data.network

import okhttp3.MultipartBody
import org.gdsc.data.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.model.request.LogoutRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserAPI {

    @POST("api/v1/user/nickname")
    suspend fun postNickname(@Body nicknameRequest: NicknameRequest): Response<NicknameResponse>

    @Multipart
    @POST("api/v1/user/profileImg")
    suspend fun postProfileImg(@Part profileImageFile: MultipartBody.Part): org.gdsc.domain.model.Response<String>

    @POST("api/v1/user/defaultProfileImg")
    suspend fun postDefaultProfileImg(): Response<String>

    @GET("api/v1/user/{nickname}")
    suspend fun checkDuplicatedNickname(@Path(value = "nickname") targetNickname: String): Response<Boolean>

    @GET("api/v1/user/info")
    suspend fun getUserInfo(): Response<UserInfo>

    @HTTP(method="DELETE", path="api/v1/auth/user", hasBody = true)
    suspend fun postUserLogOut(
        @Body request: LogoutRequest
    ): Response<String>

    @DELETE("api/v1/user")
    suspend fun postUserSignOut(): Response<String>

}