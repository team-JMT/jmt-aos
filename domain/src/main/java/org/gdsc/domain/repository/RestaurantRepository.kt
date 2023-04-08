package org.gdsc.domain.repository

import org.gdsc.domain.model.RestaurantLocationInfo

interface RestaurantRepository {

    suspend fun getRestaurantLocationInfo(query: String, page: Int): List<RestaurantLocationInfo>

    suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean
}