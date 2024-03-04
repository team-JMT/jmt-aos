package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantBySearchUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(
        keyword: String?,
        userLocation: Location?
    ): Flow<PagingData<RegisteredRestaurant>> {

        return restaurantRepository.getRegisteredRestaurantsBySearch( keyword, userLocation)
    }
}