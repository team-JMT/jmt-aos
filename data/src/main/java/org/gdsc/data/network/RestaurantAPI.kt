package org.gdsc.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdsc.data.database.RegisteredRestaurantPaging
import org.gdsc.data.database.ReviewPaging
import org.gdsc.data.model.Response
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.UserLocation
import org.gdsc.domain.model.request.RestaurantSearchRequest
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse
import org.gdsc.domain.model.response.RestaurantRegistrationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantAPI {

    @GET("api/v1/restaurant/location")
    suspend fun getRestaurantLocationInfo(
        @Query("query") query: String,
        @Query("y") latitude: String,
        @Query("x") longitude: String,
        @Query("page") page: Int,
    ): Response<List<RestaurantLocationInfo>>

    @POST("api/v1/restaurant/{recommendRestaurantId}")
    suspend fun getRecommendRestaurantInfo(
        @Path("recommendRestaurantId") recommendRestaurantId: Int,
        @Body userLocation: UserLocation,
    ): Response<RestaurantInfoResponse>

    @GET("api/v1/restaurant/registration/{kakaoSubId}")
    suspend fun checkRestaurantRegistration(
        @Path("kakaoSubId") kakaoSubId: String,
    ): Response<Boolean>

    @POST("api/v1/restaurant/location")
    suspend fun postRestaurantLocationInfo(
        @Body restaurantLocationInfo: RestaurantLocationInfo,
    ): Response<String>

    @Multipart
    @POST("api/v1/restaurant")
    suspend fun postRestaurantInfo(
        @PartMap restaurantRegistrationRequest: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part pictures: List<MultipartBody.Part>,
    ): Response<RestaurantRegistrationResponse>

    @POST("api/v1/restaurant/search")
    suspend fun getRegisteredRestaurantsBySearch(
        @Body restaurantSearchRequest: RestaurantSearchRequest,
    ): Response<RegisteredRestaurantPaging>

    @POST("api/v1/restaurant/search/{userid}")
    suspend fun getRegisteredRestaurants(
        @Path("userid") userId: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: String? = null,
        @Body restaurantSearchRequest: RestaurantSearchRequest,
    ): Response<RegisteredRestaurantPaging>

    @PUT("api/v1/restaurant")
    suspend fun putRestaurantInfo(
        @Body putRestaurantInfoRequest: ModifyRestaurantInfoRequest,
    ): Response<String>

    @POST("/api/v1/restaurant/search/map")
    suspend fun getRestaurantLocationInfoByMap(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sort") sort: Array<String>? = null,
        @Body restaurantSearchRequest: RestaurantSearchRequest,
    ): Response<RegisteredRestaurantPaging>

    @GET("/api/v1/restaurant/{recommendRestaurantId}/review")
    suspend fun getRestaurantReviews(
        @Path("recommendRestaurantId") recommendRestaurantId: Int,
    ): Response<ReviewPaging>

    @Multipart
    @POST("/api/v1/restaurant/{recommendRestaurantId}/review")
    suspend fun postRestaurantReview(
        @Path("recommendRestaurantId") recommendRestaurantId: Int,
        @Part reviewContent: MultipartBody.Part,
        @Part reviewImages: List<MultipartBody.Part>,
    ): Response<String>

}