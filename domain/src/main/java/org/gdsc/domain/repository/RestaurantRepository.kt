package org.gdsc.domain.repository

import org.gdsc.domain.model.RestaurantLocationInfo

interface RestaurantRepository {

    suspend fun getRestaurantLocationInfo(query: String, page: Int): List<RestaurantLocationInfo>
}