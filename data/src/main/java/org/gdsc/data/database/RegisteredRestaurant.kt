package org.gdsc.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RegisteredRestaurant(
    @PrimaryKey val id: Int, // 식당 id
    val name: String, // 식당 이름
    val placeUrl: String, // 식당 url
    val phone: String, // 식당 전화번호
    val address: String, // 식당 주소
    val roadAddress: String, // 식당 도로명 주소
    val x: Double, // 식당 x좌표
    val y: Double, // 식당 y좌표
    val restaurantImageUrl: String?, // 식당 이미지 url
    val introduce: String, // 식당 소개
    val category: String, // 식당 카테고리
    val userId: Int, // 식당 등록한 유저 id
    val userNickName: String, // 식당 등록한 유저 닉네임
    val userProfileImageUrl: String, // 식당 등록한 유저 프로필 이미지
    val canDrinkLiquor: Boolean, // 식당 주류 판매 여부
    val differenceInDistance: String, // 식당과의 거리
)