package org.gdsc.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.gdsc.data.model.RegisteredRestaurantResponse
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.model.request.RestaurantSearchRequest

class RestaurantBySearchPagingSource(
    private val api: RestaurantAPI,
    private val restaurantSearchRequest: RestaurantSearchRequest,
): PagingSource<Int, RegisteredRestaurantResponse>()  {
    override fun getRefreshKey(state: PagingState<Int, RegisteredRestaurantResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RegisteredRestaurantResponse> {
        val page = params.key ?: 1
        return try {
            val items = api.getRegisteredRestaurantsBySearch(
                restaurantSearchRequest = restaurantSearchRequest
            )
            LoadResult.Page(
                data = items.data.restaurants,
                prevKey = null,
                nextKey = if (items.data.restaurants.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}