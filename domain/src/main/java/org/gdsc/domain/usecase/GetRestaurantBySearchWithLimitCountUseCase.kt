package org.gdsc.domain.usecase

import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantBySearchWithLimitCountUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        keyword: String?,
        userLocation: Location?,
        limit: Int
    ): List<RegisteredRestaurant> {

        return restaurantRepository.getRegisteredRestaurantsBySearchWithLimitCount(keyword, userLocation, limit)
    }
}