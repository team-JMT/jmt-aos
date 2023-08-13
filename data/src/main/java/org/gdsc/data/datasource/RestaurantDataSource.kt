package org.gdsc.data.datasource

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.data.database.RegisteredRestaurant
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchMapRequest

interface RestaurantDataSource {

    suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo>

    suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean

    suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String

    suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String

    suspend fun getRestaurants(userId: Int, restaurantSearchMapRequest: RestaurantSearchMapRequest): Flow<PagingData<RegisteredRestaurant>>
}