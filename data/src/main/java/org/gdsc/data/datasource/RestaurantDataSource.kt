package org.gdsc.data.datasource

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import org.gdsc.data.database.RegisteredRestaurant
import org.gdsc.data.database.ReviewPaging
import org.gdsc.data.model.RegisteredRestaurantResponse
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.PagingResult
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.UserLocation
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse

interface RestaurantDataSource {

    suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo>

    suspend fun getRecommendRestaurantInfo(recommendRestaurantId: Int,  userLocation: UserLocation): RestaurantInfoResponse

    suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean

    suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String

    suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String

    suspend fun getRestaurants(
        userId: Int, locationData: Location, sortType: SortType, foodCategory: FoodCategory, drinkPossibility: DrinkPossibility
        ): Flow<PagingResult<RegisteredRestaurant>>

    suspend fun putRestaurantInfo(putRestaurantInfoRequest: ModifyRestaurantInfoRequest): String

    suspend fun getRestaurantsByMap(
        userLocation: Location?, startLocation: Location?, endLocation: Location?, sortType: SortType, foodCategory: FoodCategory?, drinkPossibility: DrinkPossibility?
    ): Flow<PagingData<RegisteredRestaurantResponse>>

    suspend fun getRegisteredRestaurantsBySearch(keyword: String?, userLocation: Location?): Flow<PagingData<RegisteredRestaurantResponse>>

    suspend fun getRegisteredRestaurantsBySearchWithLimitCount(keyword: String?, userLocation: Location?, limit: Int): List<RegisteredRestaurantResponse>

    suspend fun getRestaurantReviews(restaurantId: Int): ReviewPaging

    suspend fun postRestaurantReview(restaurantId: Int, reviewContent: String, reviewImages: List<MultipartBody.Part>): Boolean

}