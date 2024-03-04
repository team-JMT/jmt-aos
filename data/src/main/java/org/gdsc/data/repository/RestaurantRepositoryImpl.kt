package org.gdsc.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gdsc.data.datasource.RestaurantDataSource
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.PagingResult
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.UserLocation
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val restaurantDataSource: RestaurantDataSource
) : RestaurantRepository {
    override suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo> {
        return restaurantDataSource.getRestaurantLocationInfo(query, latitude, longitude, page)
    }

    override suspend fun getRecommendRestaurantInfo(recommendRestaurantId: Int,  userLocation: UserLocation): RestaurantInfoResponse {
        return restaurantDataSource.getRecommendRestaurantInfo(recommendRestaurantId, userLocation)
    }

    override suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean {
        return restaurantDataSource.checkRestaurantRegistration(kakaoSubId)
    }

    override suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String {
        return restaurantDataSource.postRestaurantLocationInfo(restaurantLocationInfo)
    }

    override suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String {
        return restaurantDataSource.postRestaurantInfo(restaurantRegistrationRequest)
    }

    override suspend fun getRestaurants(
        userId: Int, locationData: Location, sortType: SortType, foodCategory: FoodCategory, drinkPossibility: DrinkPossibility
    ): Flow<PagingResult<RegisteredRestaurant>> {
        return restaurantDataSource.getRestaurants(
            userId,
            locationData,
            sortType,
            foodCategory,
            drinkPossibility
        ).map { result ->

            val pagingTemp = PagingResult(
                result.data.map { restaurant ->
                    val restaurantTemp = RegisteredRestaurant(
                        id = restaurant.id,
                        name = restaurant.name,
                        placeUrl = restaurant.placeUrl,
                        phone = restaurant.phone,
                        address = restaurant.address,
                        roadAddress = restaurant.roadAddress,
                        x = restaurant.x,
                        y = restaurant.y,
                        restaurantImageUrl = restaurant.restaurantImageUrl,
                        introduce = restaurant.introduce,
                        category = restaurant.category,
                        userId = userId,
                        userNickName = restaurant.userNickName,
                        userProfileImageUrl = restaurant.userProfileImageUrl,
                        canDrinkLiquor = restaurant.canDrinkLiquor,
                        differenceInDistance = restaurant.differenceInDistance,
                    )
                    restaurantTemp
                }, result.totalElementsCount)
            pagingTemp
        }
    }

    override suspend fun putRestaurantInfo(putRestaurantInfoRequest: ModifyRestaurantInfoRequest): String {
        return restaurantDataSource.putRestaurantInfo(putRestaurantInfoRequest)

    }

    override suspend fun getRestaurantsByMap(
        userLocation: Location?, startLocation: Location?, endLocation: Location?, sortType: SortType, foodCategory: FoodCategory?, drinkPossibility: DrinkPossibility?
    ): Flow<PagingData<RegisteredRestaurant>> {
        return restaurantDataSource.getRestaurantsByMap(
            userLocation,
            startLocation,
            endLocation,
            sortType,
            foodCategory,
            drinkPossibility
        ).map { result ->
            result.map { restaurant ->
                RegisteredRestaurant(
                    id = restaurant.id,
                    name = restaurant.name,
                    placeUrl = restaurant.placeUrl,
                    phone = restaurant.phone,
                    address = restaurant.address,
                    roadAddress = restaurant.roadAddress,
                    x = restaurant.x,
                    y = restaurant.y,
                    restaurantImageUrl = restaurant.restaurantImageUrl,
                    introduce = restaurant.introduce,
                    category = restaurant.category,
                    userId = restaurant.id,
                    userNickName = restaurant.userNickName,
                    userProfileImageUrl = restaurant.userProfileImageUrl,
                    canDrinkLiquor = restaurant.canDrinkLiquor,
                    differenceInDistance = restaurant.differenceInDistance,
                )
            }
        }
    }

    override suspend fun getRegisteredRestaurantsBySearch(keyword: String?, userLocation: Location?): Flow<PagingData<RegisteredRestaurant>> {
        return restaurantDataSource.getRegisteredRestaurantsBySearch(keyword, userLocation).map { result ->
            result.map { restaurant ->
                RegisteredRestaurant(
                    id = restaurant.id,
                    name = restaurant.name,
                    placeUrl = restaurant.placeUrl,
                    phone = restaurant.phone,
                    address = restaurant.address,
                    roadAddress = restaurant.roadAddress,
                    x = restaurant.x,
                    y = restaurant.y,
                    restaurantImageUrl = restaurant.restaurantImageUrl,
                    introduce = restaurant.introduce,
                    category = restaurant.category,
                    userId = restaurant.id,
                    userNickName = restaurant.userNickName,
                    userProfileImageUrl = restaurant.userProfileImageUrl,
                    canDrinkLiquor = restaurant.canDrinkLiquor,
                    differenceInDistance = restaurant.differenceInDistance,
                )
            }
        }
    }
}