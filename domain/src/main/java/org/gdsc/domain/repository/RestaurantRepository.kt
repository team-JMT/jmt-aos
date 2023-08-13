package org.gdsc.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchMapRequest

interface RestaurantRepository {

    suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo>

    suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean

    suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String

    suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String

    suspend fun getRestaurants(userId: Int, restaurantSearchMapRequest: RestaurantSearchMapRequest): Flow<PagingData<RegisteredRestaurant>>
}