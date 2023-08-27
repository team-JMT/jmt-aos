package org.gdsc.data.repository

import org.gdsc.data.datasource.RestaurantDataSource
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse
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

    override suspend fun getRecommendRestaurantInfo(recommendRestaurantId: Int): RestaurantInfoResponse {
        return restaurantDataSource.getRecommendRestaurantInfo(recommendRestaurantId)
    }

    override suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean {
        return restaurantDataSource.checkRestaurantRegistration(kakaoSubId)
    }

    override suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String {
        return restaurantDataSource.postRestaurantLocationInfo(restaurantLocationInfo)
    }

    override suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String {
        return restaurantDataSource.postRestaurantInfo(restaurantRegistrationRequest)
    }

    override suspend fun putRestaurantInfo(putRestaurantInfoRequest: ModifyRestaurantInfoRequest): String {
        return restaurantDataSource.putRestaurantInfo(putRestaurantInfoRequest)
    }
}