package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.PagingResult
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.request.RestaurantSearchMapRequest
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRegisteredRestaurantUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        userId: Int, locationData: Location, sortType: SortType, foodCategory: FoodCategory, drinkPossibility: DrinkPossibility
    ): Flow<PagingResult<RegisteredRestaurant>> {

        return restaurantRepository.getRestaurants(userId, locationData, sortType, foodCategory, drinkPossibility)
    }
}