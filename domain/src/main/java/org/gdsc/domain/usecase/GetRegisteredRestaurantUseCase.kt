package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.request.RestaurantSearchMapRequest
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRegisteredRestaurantUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(userId: Int, restaurantSearchMapRequest: RestaurantSearchMapRequest): Flow<PagingData<RegisteredRestaurant>> {

        return restaurantRepository.getRestaurants(userId, restaurantSearchMapRequest)
    }
}