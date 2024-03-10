package org.gdsc.domain.usecase

import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantReviewsUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(recommendRestaurantId: Int) = restaurantRepository.getRestaurantReviews(recommendRestaurantId)
}