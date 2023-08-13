package org.gdsc.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RestaurantDao {

    //전체 Post 목록을 PagingSource로 가져온다.  {추후에 Date 데이터가 추가 되면 변경해야 한다.}
    @Query("SELECT * FROM restaurant WHERE userId = :userId ORDER BY id DESC")
    fun getRestaurants(userId: Int): PagingSource<Int, RegisteredRestaurant>

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