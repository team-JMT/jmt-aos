package org.gdsc.domain.usecase

import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class PutRestaurantInfoUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
){
    suspend operator fun invoke(putRestaurantInfoRequest: ModifyRestaurantInfoRequest) {
        restaurantRepository.putRestaurantInfo(putRestaurantInfoRequest)
    }
}