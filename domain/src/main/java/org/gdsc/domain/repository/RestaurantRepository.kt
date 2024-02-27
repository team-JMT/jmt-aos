package org.gdsc.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.PagingResult
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchMapRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse

interface RestaurantRepository {

    suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo>

    suspend fun getRecommendRestaurantInfo(recommendRestaurantId: Int): RestaurantInfoResponse

    suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean

    suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String

    suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String

    suspend fun getRestaurants(
        userId: Int, locationData: Location, sortType: SortType, foodCategory: FoodCategory, drinkPossibility: DrinkPossibility
        ): Flow<PagingResult<RegisteredRestaurant>>

    suspend fun putRestaurantInfo(putRestaurantInfoRequest: ModifyRestaurantInfoRequest): String

    suspend fun getRestaurantsByMap(
        userLocation: Location?, startLocation: Location?, endLocation: Location?, sortType: SortType, foodCategory: FoodCategory?, drinkPossibility: DrinkPossibility?
    ): Flow<PagingData<RegisteredRestaurant>>

}