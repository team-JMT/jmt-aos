package org.gdsc.data.network

import org.gdsc.data.model.Response
import org.gdsc.domain.model.RestaurantLocationInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantAPI {

    @GET("api/v1/restaurant/location")
    suspend fun getRestaurantLocationInfo(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): Response<List<RestaurantLocationInfo>>
}