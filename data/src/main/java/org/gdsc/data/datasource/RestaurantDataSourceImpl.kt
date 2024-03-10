package org.gdsc.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.RequestBody.Companion.toRequestBody
import org.gdsc.data.database.RegisteredRestaurant
import org.gdsc.data.database.RestaurantByMapPagingSource
import org.gdsc.data.database.RestaurantBySearchPagingSource
import org.gdsc.data.database.RestaurantDatabase
import org.gdsc.data.database.RestaurantMediator
import org.gdsc.data.database.ReviewPaging
import org.gdsc.data.model.RegisteredRestaurantResponse
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.Empty
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.RestaurantRegistrationState
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Filter
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.PagingResult
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.model.UserLocation
import org.gdsc.domain.model.request.ModifyRestaurantInfoRequest
import org.gdsc.domain.model.request.RestaurantRegistrationRequest
import org.gdsc.domain.model.request.RestaurantSearchRequest
import org.gdsc.domain.model.response.RestaurantInfoResponse
import retrofit2.HttpException
import javax.inject.Inject

class RestaurantDataSourceImpl @Inject constructor(
    private val restaurantAPI: RestaurantAPI,
    private val db: RestaurantDatabase,
) : RestaurantDataSource {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    override suspend fun getRestaurantLocationInfo(
        query: String,
        latitude: String,
        longitude: String,
        page: Int
    ): List<RestaurantLocationInfo> {
        // TODO: 예외 처리
        return restaurantAPI.getRestaurantLocationInfo(query, latitude, longitude, page).data
    }

    override suspend fun getRecommendRestaurantInfo(
        recommendRestaurantId: Int,
        userLocation: UserLocation
    ): RestaurantInfoResponse {
        return restaurantAPI.getRecommendRestaurantInfo(recommendRestaurantId, userLocation).data
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
        runCatching {
            restaurantAPI.postRestaurantInfo(
                mapOf(
                    "name" to restaurantRegistrationRequest.name.toRequestBody(),
                    "introduce" to restaurantRegistrationRequest.introduce.toRequestBody(),
                    "categoryId" to restaurantRegistrationRequest.categoryId.toString()
                        .toRequestBody(),
                    "canDrinkLiquor" to restaurantRegistrationRequest.canDrinkLiquor.toString()
                        .toRequestBody(),
                    "goWellWithLiquor" to restaurantRegistrationRequest.goWellWithLiquor.toRequestBody(),
                    "recommendMenu" to restaurantRegistrationRequest.recommendMenu.toRequestBody(),
                    "restaurantLocationId" to restaurantRegistrationRequest.restaurantLocationAggregateId.toRequestBody(),
                    "groupId" to "10".toRequestBody()
                ),
                pictures = restaurantRegistrationRequest.pictures
            )
        }.onSuccess {
            return it.data.recommendRestaurantId
        }.onFailure { throwable ->
            if (throwable is HttpException) {
                Log.e("postRestaurantInfo", throwable.message() ?: "")
                when (throwable.code()) {
                    RestaurantRegistrationState.NO_EXIST.code -> {
                        return ""
                    }

                    RestaurantRegistrationState.ALL_EXIST.code -> {
                        return ""
                    }
                }
            }
        }
        return ""
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRestaurants(
        userId: Int,
        locationData: Location,
        sortType: SortType,
        foodCategory: FoodCategory,
        drinkPossibility: DrinkPossibility
    ): Flow<PagingResult<RegisteredRestaurant>> {
        val categoryFilter = when (foodCategory) {
            FoodCategory.INIT, FoodCategory.ETC -> null
            else -> foodCategory.text
        }

        val isCanDrinkLiquor = when (drinkPossibility) {
            DrinkPossibility.POSSIBLE -> true
            DrinkPossibility.IMPOSSIBLE -> false
            else -> null
        }

        val filter = Filter(
            categoryFilter = when (foodCategory) {
                FoodCategory.INIT, FoodCategory.ETC -> String.Empty
                else -> foodCategory.key
            },
            isCanDrinkLiquor = isCanDrinkLiquor,
        )

        val restaurantSearchRequest = RestaurantSearchRequest(filter, locationData)
        val mediator = RestaurantMediator(
            userId = userId,
            restaurantSearchRequest = restaurantSearchRequest,
            db = db,
            api = restaurantAPI,
        )

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = mediator,
        ) {
            with(db.restaurantDao()) {
                when (sortType) {
                    SortType.DISTANCE -> getRegisteredRestaurantsSortedDistance(
                        userId,
                        categoryFilter,
                        isCanDrinkLiquor
                    )

                    SortType.RECENCY -> getRegisteredRestaurantsSortedRecent(
                        userId,
                        categoryFilter,
                        isCanDrinkLiquor
                    )

                    SortType.LIKED -> getRegisteredRestaurants(
                        userId,
                        categoryFilter,
                        isCanDrinkLiquor
                    )
                }
            }

        }.flow
            .map { data ->
                val totalElementsCount = mediator.totalElementsCount
                PagingResult(data, totalElementsCount)
            }
    }

    override suspend fun putRestaurantInfo(putRestaurantInfoRequest: ModifyRestaurantInfoRequest): String {
        return restaurantAPI.putRestaurantInfo(putRestaurantInfoRequest).data
    }

    override suspend fun getRestaurantsByMap(
        userLocation: Location?,
        startLocation: Location?,
        endLocation: Location?,
        sortType: SortType,
        foodCategory: FoodCategory?,
        drinkPossibility: DrinkPossibility?
    ): Flow<PagingData<RegisteredRestaurantResponse>> {
        val restaurantSearchRequest = RestaurantSearchRequest(
            userLocation = userLocation,
            startLocation = startLocation,
            endLocation = endLocation,
            filter = Filter(
                categoryFilter = when (foodCategory) {
                    FoodCategory.INIT, FoodCategory.ETC -> String.Empty
                    null -> null
                    else -> foodCategory.key
                },
                isCanDrinkLiquor = when (drinkPossibility) {
                    DrinkPossibility.POSSIBLE -> true
                    DrinkPossibility.IMPOSSIBLE -> false
                    else -> null
                },
            )
        )
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            )
        ) {
            RestaurantByMapPagingSource(
                restaurantAPI,
                restaurantSearchRequest
            )
        }.flow.cachedIn(coroutineScope)
    }

    override suspend fun getRegisteredRestaurantsBySearch(
        keyword: String?, userLocation: Location?
    ): Flow<PagingData<RegisteredRestaurantResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            )
        ) {
            RestaurantBySearchPagingSource(
                restaurantAPI,
                RestaurantSearchRequest(
                    keyword = keyword,
                    userLocation = userLocation
                )
            )
        }.flow.cachedIn(coroutineScope)
    }

    override suspend fun getRegisteredRestaurantsBySearchWithLimitCount(
        keyword: String?,
        userLocation: Location?,
        limit: Int
    ): List<RegisteredRestaurantResponse> {

        return restaurantAPI.
        getRegisteredRestaurantsBySearch(
            RestaurantSearchRequest(
                keyword = keyword,
                userLocation = userLocation
            )
        ).data.restaurants.take(limit)
    }

    override suspend fun getRestaurantReviews(restaurantId: Int): ReviewPaging {
        return restaurantAPI.getRestaurantReviews(restaurantId).data
    }

}
