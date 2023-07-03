package org.gdsc.domain.usecase

import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class PostRestaurantLocationInfoUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(restaurantLocationInfo: RestaurantLocationInfo): String {
        return restaurantRepository.postRestaurantLocationInfo(restaurantLocationInfo)
    }
}