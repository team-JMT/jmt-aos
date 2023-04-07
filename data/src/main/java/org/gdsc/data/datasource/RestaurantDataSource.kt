package org.gdsc.data.datasource

import org.gdsc.domain.model.RestaurantLocationInfo

interface RestaurantDataSource {

    suspend fun getRestaurantLocationInfo(query: String, page: Int): List<RestaurantLocationInfo>
}