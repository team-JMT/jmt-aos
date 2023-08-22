package org.gdsc.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import okhttp3.RequestBody.Companion.toRequestBody
import org.gdsc.data.database.RestaurantDatabase
import org.gdsc.data.database.RestaurantMediator
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.RestaurantRegistrationState
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchMapRequest
import retrofit2.HttpException
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val restaurantAPI: RestaurantAPI,
    private val db: RestaurantDatabase,
) : RestaurantDataSource {

    override suspend fun getRestaurantLocationInfo(
        query: String,
        latitude: String,
        longitude: String,
        page: Int
    ): List<RestaurantLocationInfo> {
        // TODO: 예외 처리
        return restaurantAPI.getRestaurantLocationInfo(query, latitude, longitude, page).data
    }

    override suspend fun checkRestaurantRegistration(kakaoSubId: String): Boolean {
        runCatching {
            restaurantAPI.checkRestaurantRegistration(kakaoSubId)
        }.onSuccess {
            return true
        }.onFailure { throwable ->
            if (throwable is HttpException) {
                when (throwable.code()) {
                    RestaurantRegistrationState.NO_EXIST.code -> {
                        return true
                    }

                    RestaurantRegistrationState.ALL_EXIST.code -> {
                        return false
                    }
                }
            }
        }
        return true
    }

    override suspend fun postRestaurantLocationInfo(restaurantLocationInfo: RestaurantLocationInfo): String {
        return restaurantAPI.postRestaurantLocationInfo(restaurantLocationInfo).data
    }

    override suspend fun postRestaurantInfo(restaurantRegistrationRequest: RestaurantRegistrationRequest): String {
        return restaurantAPI.postRestaurantInfo(
            mapOf(
                "name" to restaurantRegistrationRequest.name.toRequestBody(),
                "introduce" to restaurantRegistrationRequest.introduce.toRequestBody(),
                "categoryId" to restaurantRegistrationRequest.categoryId.toString().toRequestBody(),
                "canDrinkLiquor" to restaurantRegistrationRequest.canDrinkLiquor.toString()
                    .toRequestBody(),
                "goWellWithLiquor" to restaurantRegistrationRequest.goWellWithLiquor.toRequestBody(),
                "recommendMenu" to restaurantRegistrationRequest.recommendMenu.toRequestBody(),
                "restaurantLocationId" to restaurantRegistrationRequest.restaurantLocationAggregateId.toRequestBody()
            ),
            pictures = restaurantRegistrationRequest.pictures
        ).data.recommendRestaurantId
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRestaurants(userId: Int, restaurantSearchMapRequest: RestaurantSearchMapRequest) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        remoteMediator = RestaurantMediator(
            userId = userId,
            restaurantSearchMapRequest = restaurantSearchMapRequest,
            db = db,
            api = restaurantAPI,
        )
    ) {
        with(restaurantSearchMapRequest.filter) {
            db.restaurantDao().getRestaurants(userId, categoryFilter, isCanDrinkLiquor)
        }
    }.flow
}
