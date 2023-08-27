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
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchMapRequest
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
    ): Flow<PagingData<RegisteredRestaurant>> {
        return restaurantDataSource.getRestaurants(userId, locationData, sortType, foodCategory, drinkPossibility).map { pagingData ->
            val pagingTemp = pagingData.map { restaurant ->
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
            }
            pagingTemp
        }
    }
}