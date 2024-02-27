package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantsByMapUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        sortType: SortType, foodCategory: FoodCategory?, drinkPossibility: DrinkPossibility?, userLocation: Location?, startLocation: Location? = null, endLocation: Location? = null
    ): Flow<PagingData<RegisteredRestaurant>> {

        return restaurantRepository.getRestaurantsByMap( userLocation, startLocation, endLocation, sortType, foodCategory, drinkPossibility)
    }
}