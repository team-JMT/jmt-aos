package org.gdsc.data.repository

import org.gdsc.data.datasource.RestaurantDataSource
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDataSource: RestaurantDataSource
) : RestaurantRepository {
    override suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo> {
        return restaurantDataSource.getRestaurantLocationInfo(query, latitude, longitude, page)
    }

    override suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean {
        return restaurantDataSource.checkRestaurantRegistration(kakaoSubId)
    }

    override suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String {
        return restaurantDataSource.postRestaurantLocationInfo(restaurantLocationInfo)
    }
}