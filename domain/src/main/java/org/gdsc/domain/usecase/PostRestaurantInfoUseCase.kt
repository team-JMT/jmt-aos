package org.gdsc.domain.usecase

import okhttp3.MultipartBody
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class PostRestaurantInfoUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(
        name: String,
        introduce: String,
        categoryId: Long,
        pictures: List<MultipartBody.Part>,
        canDrinkLiquor: Boolean,
        goWellWithLiquor: String,
        recommendMenu: String,
        restaurantLocationAggregateIdg: String
    ): String {
        return restaurantRepository.postRestaurantInfo(
            RestaurantRegistrationRequest(
                name,
                introduce,
                categoryId,
                canDrinkLiquor,
                pictures,
                goWellWithLiquor,
                recommendMenu,
                restaurantLocationAggregateIdg
            ),
        )
    }
}