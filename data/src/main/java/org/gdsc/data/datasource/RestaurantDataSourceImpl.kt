package org.gdsc.data.datasource

import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.model.RestaurantLocationInfo
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val restaurantAPI: RestaurantAPI
): RestaurantDataSource {
    override suspend fun getRestaurantLocationInfo(query: String, page: Int): List<RestaurantLocationInfo> {
        // TODO: 예외 처리
        return restaurantAPI.getRestaurantLocationInfo(query, page).data
    }
}