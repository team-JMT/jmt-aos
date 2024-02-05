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
//            val items = api.getRestaurantLocationInfoByMap(
//                page = page,
//                size = params.loadSize,
//                restaurantSearchMapRequest = restaurantSearchMapRequest
//            )
            val items = testData()
            LoadResult.Page(
                data = items.data.restaurants,
//                prevKey = if (page == 1) null else page - 1,
                prevKey = null,
//                nextKey = if (items.data.restaurants.isEmpty()) null else page + 1
                nextKey = null
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

fun testData(): Response<RegisteredRestaurantPaging> {
    Log.d("testLog", "testData run")
    return Response(
        code = "200",
        message = "Success",
        data = RegisteredRestaurantPaging(
            page = Page(
                totalPages = 1,
                currentPage = 0,
                totalElements = 1,
                size = 1,
                numberOfElements = 1,
                empty = false,
                pageLast = true,
                pageFirst = true
            ),
            restaurants = listOf(
                RegisteredRestaurantResponse(
                    id = 1,
                    name = "마제소바 처럼 생긴 무엇인가",
                    placeUrl = "https://naver.com",
                    phone = "010-1234-5678",
                    address = "서울시 강남구",
                    roadAddress = "서울시 강남구",
                    x = 127.0221,
                    y = 37.6318,
                    restaurantImageUrl = "https://cdn.discordapp.com/attachments/921338716927193139/1204055133269270568/78d3e5b9e53122edc8672aebf427aa2d.jpg?ex=65d356ba&is=65c0e1ba&hm=0cd15951ffa0acf3be6a4fc4ba815469a6168f5421da3dede9443c99dee5fab8&",
                    introduce = "어떤 음식인지 궁금하지 않으신가요? 일단 이미지를 가져왔지만 어떤 음식인지 몰라 이렇게 소개드립니다.",
                    category = "일식",
                    userNickName = "전병선이오",
                    userProfileImageUrl = "https://cdn.discordapp.com/attachments/921338716927193139/1204056270538276904/123.jpg?ex=65d357c9&is=65c0e2c9&hm=b7ee3c8423440a328d95f743e37609bdb90f23d522600f2c5b996148fd2ef89b&",
                    canDrinkLiquor = true,
                    differenceInDistance = "10"
                ),
                RegisteredRestaurantResponse(
                    id = 2,
                    name = "연어 월남쌈 1호점",
                    placeUrl = "https://naver.com",
                    phone = "010-1234-5678",
                    address = "서울시 강남구",
                    roadAddress = "서울시 강남구",
                    x = 127.0223,
                    y = 37.6319,
                    restaurantImageUrl = "https://cdn.discordapp.com/attachments/921338716927193139/1204055133554610218/23808ac545ccc97a9b28d04198ae3b37.jpg?ex=65d356ba&is=65c0e1ba&hm=64da418e7fbb45e9416eda93ac0256ccfd9d3830be2f554674db1cb3f6f68552&",
                    introduce = "솔직히 진짜 맛있어 보이는데 저도 한입 먹어보겠습니다. 하지만 연어는 구이라는 사실을 이 분은 아직 모르셨나봅니다.",
                    category = "한식",
                    userNickName = "참치 킬러",
                    userProfileImageUrl = "https://cdn.discordapp.com/attachments/921338716927193139/1204056270102073384/fishKiller.jpg?ex=65d357c9&is=65c0e2c9&hm=83b8fce12a78c338c199b99db9c974b5fee57d6b3e875ad8ca26a41fdedb7c4c&",
                    canDrinkLiquor = false,
                    differenceInDistance = "10"
                ))
        )
    )
}