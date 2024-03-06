package org.gdsc.domain.model.response

data class RestaurantInfoResponse(
    val address: String,
    val canDrinkLiquor: Boolean,
    val category: String,
    val goWellWithLiquor: String,
    val introduce: String,
    val name: String,
    val phone: String,
    val pictures: List<String>,
    val placeUrl: String,
    val recommendMenu: String,
    val roadAddress: String,
    val userId: Int,
    val userNickName: String,
    val userProfileImageUrl: String,
    val x: Double,
    val y: Double,
    val differenceInDistance: String
)