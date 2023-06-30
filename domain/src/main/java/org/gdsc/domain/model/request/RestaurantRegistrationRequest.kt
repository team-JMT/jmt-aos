package org.gdsc.domain.model.request

import okhttp3.MultipartBody

data class RestaurantRegistrationRequest(
    val name: String,
    val introduce: String,
    val categoryId: Long,
    val canDrinkLiquor: Boolean,
    val pictures: List<MultipartBody.Part>,
    val goWellWithLiquor: String,
    val recommendMenu: String,
    val restaurantLocationAggregateId: String
)