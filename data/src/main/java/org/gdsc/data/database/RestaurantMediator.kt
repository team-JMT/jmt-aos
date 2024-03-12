package org.gdsc.data.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.model.request.RestaurantSearchRequest
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RestaurantMediator(
    private val userId: Int,
    private val restaurantSearchRequest: RestaurantSearchRequest,
    private val db: RestaurantDatabase,
    private val api: RestaurantAPI,
): RemoteMediator<Int,RegisteredRestaurant>()  {
    private val userDao = db.restaurantDao()
    private var currentPageNumber = 0
    var totalElementsCount = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int,RegisteredRestaurant>,
    ): MediatorResult {
        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> null

                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    lastItem.id
                }
            }

            val response = api.getRegisteredRestaurants(
                userId = userId,
                page = currentPageNumber,
                size = state.config.pageSize,
                restaurantSearchRequest = restaurantSearchRequest
            )

            val repos = response.data
            currentPageNumber = repos.page.currentPage
            totalElementsCount = repos.page.totalElements
            val endOfPaginationReached = repos.page.pageLast
            if(repos.restaurants.isNotEmpty()) {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
//                    userDao.deleteByQuery(query)
                    }
                    currentPageNumber = repos.page.currentPage + 1

                    userDao.insertAll(
                        repos.restaurants.map {
                            it.convertResponseToRegisteredRestaurant(
                                userId = userId
                            )
                        }
                    )
                }

                MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached
                )
            } else {
                MediatorResult.Error(
                    Exception("No data")
                )
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}