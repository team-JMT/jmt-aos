package org.gdsc.domain.usecase

import org.gdsc.domain.model.UserLocation
import org.gdsc.domain.model.response.RestaurantInfoResponse
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantInfoUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(
        recommendRestaurantId: Int,
        x: String,
        y: String
    ): RestaurantInfoResponse {
        return restaurantRepository.getRecommendRestaurantInfo(recommendRestaurantId, UserLocation(x, y))
    }
}