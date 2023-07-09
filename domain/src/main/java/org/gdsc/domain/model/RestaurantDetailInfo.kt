package org.gdsc.domain.model

import okhttp3.MultipartBody

data class RestaurantDetailInfo (
    val introduce: String,
    val categoryId: Long,
    val pictures: List<MultipartBody.Part>,
    val canDrinkLiquor: Boolean,
    val goWellWithLiquor: String,
    val recommendMenu: String,
    val restaurantLocationAggregateIdg: String,
)