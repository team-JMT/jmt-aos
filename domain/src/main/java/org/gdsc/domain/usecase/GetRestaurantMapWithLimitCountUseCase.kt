package org.gdsc.domain.usecase

import org.gdsc.domain.SortType
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.request.RestaurantSearchRequest
import org.gdsc.domain.model.response.Group
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantMapWithLimitCountUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        sortType: SortType,
        group: Group?,
    ): List<RegisteredRestaurant> {
        return restaurantRepository.getRegisteredRestaurantByMapWithLimitCount(sortType, group)
    }
}