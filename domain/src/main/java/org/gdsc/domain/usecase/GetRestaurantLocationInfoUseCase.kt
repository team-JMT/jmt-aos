package org.gdsc.domain.usecase

import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantLocationInfoUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo> {
        return restaurantRepository.getRestaurantLocationInfo(query, latitude, longitude, page)
    }
}
