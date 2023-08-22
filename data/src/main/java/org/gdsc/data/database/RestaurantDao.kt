package org.gdsc.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RestaurantDao {

    // Post 목록을 PagingSource로 가져온다.
    // 필요 시에는 전체와 Filter 첨부된 쿼리로 나눠도 괜찮을 듯 함.
    @Query("SELECT * FROM restaurant WHERE userId = :userId" +
            " AND(:canDrinkLiquor IS NULL OR canDrinkLiquor = :canDrinkLiquor)" +
            " AND (:category IS NULL OR category = :category)" +
            " ORDER BY id DESC")
    fun getRestaurants(userId: Int, category: String?, canDrinkLiquor: Boolean?): PagingSource<Int, RegisteredRestaurant>

    //가장 최근 Post를 하나 가져온다.
    @Query("SELECT * FROM restaurant ORDER BY id DESC LIMIT 1")
    fun getLatestRestaurant(): RegisteredRestaurant?

    //가장 오래 된 Post를 하나 가져온다.
    @Query("SELECT * FROM restaurant ORDER BY id ASC LIMIT 1")
    fun getEarliestRestaurant(): RegisteredRestaurant?

    //Post 목록 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registeredRestaurant : List<RegisteredRestaurant>)

    // Post 데이터 전체 삭제
    @Query("DELETE FROM restaurant")
    fun deleteAll()

//    @Query("DELETE FROM restaurant WHERE id = :query")
//    fun deleteByQuery(query: String)
}