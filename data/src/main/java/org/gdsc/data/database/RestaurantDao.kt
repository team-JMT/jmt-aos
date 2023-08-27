package org.gdsc.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RestaurantDao {

    /**
    * 특정 유저가 등록한 맛집 리스트 가져오기
     */
    @Query("SELECT * FROM restaurant WHERE userId = :userId" +
            " AND(:canDrinkLiquor IS NULL OR canDrinkLiquor = :canDrinkLiquor)" +
            " AND (:category IS NULL OR category = :category) ")
    fun getRegisteredRestaurants(userId: Int, category: String?, canDrinkLiquor: Boolean?): PagingSource<Int, RegisteredRestaurant>

    /**
     * 특정 유저가 등록한 맛집 리스트 가져오기 + 최신순
     */
    @Query("SELECT * FROM restaurant WHERE userId = :userId" +
            " AND(:canDrinkLiquor IS NULL OR canDrinkLiquor = :canDrinkLiquor)" +
            " AND (:category IS NULL OR category = :category) " +
            " ORDER BY id DESC")
    fun getRegisteredRestaurantsSortedRecent(userId: Int, category: String?, canDrinkLiquor: Boolean?): PagingSource<Int, RegisteredRestaurant>

    /**
     * 특정 유저가 등록한 맛집 리스트 가져오기 + 거리순
     */
    @Query("SELECT * FROM restaurant WHERE userId = :userId" +
            " AND(:canDrinkLiquor IS NULL OR canDrinkLiquor = :canDrinkLiquor)" +
            " AND (:category IS NULL OR category = :category) " +
            " ORDER BY CAST(differenceInDistance AS INTEGER) ASC")
    fun getRegisteredRestaurantsSortedDistance(userId: Int, category: String?, canDrinkLiquor: Boolean?): PagingSource<Int, RegisteredRestaurant>

    /**
     * 특정 유저가 등록한 맛집 리스트 가져오기 + 좋아요순
     */
//    @Query("SELECT * FROM restaurant WHERE like = :like" +
//            " AND(:canDrinkLiquor IS NULL OR canDrinkLiquor = :canDrinkLiquor)" +
//            " AND (:category IS NULL OR category = :category) " +
//            " ORDER BY id DESC")
//    fun getRestaurantsSortedLike(like: Int, category: String?, canDrinkLiquor: Boolean?): PagingSource<Int, RegisteredRestaurant>



    /**
     * 특정 유저가 등록한 맛집 리스트 Room에 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registeredRestaurant : List<RegisteredRestaurant>)

    /**
     * 등록한 맛집 제거
     */
    @Query("DELETE FROM restaurant")
    fun deleteAll()

//    @Query("DELETE FROM restaurant WHERE id = :query")
//    fun deleteByQuery(query: String)
}