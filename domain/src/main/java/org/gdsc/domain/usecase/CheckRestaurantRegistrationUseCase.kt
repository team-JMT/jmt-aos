package org.gdsc.domain.usecase

import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class CheckRestaurantRegistrationUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(kakaoSubId: String): Boolean {
        return restaurantRepository.checkRestaurantRegistration(kakaoSubId)
    }
}