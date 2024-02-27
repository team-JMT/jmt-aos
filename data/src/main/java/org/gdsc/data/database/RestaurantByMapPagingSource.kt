package org.gdsc.data.database

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import org.gdsc.data.model.RegisteredRestaurantResponse
import org.gdsc.data.model.Response
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.domain.model.request.RestaurantSearchMapRequest

class RestaurantByMapPagingSource(
    private val api: RestaurantAPI,
    private val restaurantSearchMapRequest: RestaurantSearchMapRequest,
): PagingSource<Int, RegisteredRestaurantResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RegisteredRestaurantResponse> {
        val page = params.key ?: 1
        return try {
            val items = api.getRestaurantLocationInfoByMap(
                page = page,
                size = params.loadSize,
                restaurantSearchMapRequest = restaurantSearchMapRequest
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

    override fun getRefreshKey(state: PagingState<Int, RegisteredRestaurantResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}