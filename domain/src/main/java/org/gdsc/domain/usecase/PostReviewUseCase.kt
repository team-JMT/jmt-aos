package org.gdsc.domain.usecase

import okhttp3.MultipartBody
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class PostReviewUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(restaurantId: Int, reviewContent: String, reviewImages: List<MultipartBody.Part>): Boolean {
        return restaurantRepository.postRestaurantReview(restaurantId, reviewContent, reviewImages)
    }
}