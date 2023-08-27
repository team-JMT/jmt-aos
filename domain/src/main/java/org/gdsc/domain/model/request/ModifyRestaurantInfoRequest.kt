package org.gdsc.domain.model.request

data class ModifyRestaurantInfoRequest(
    val canDrinkLiquor: Boolean,
    val categoryId: Int,
    val goWellWithLiquor: String,
    val id: Int,
    val introduce: String,
    val recommendMenu: String
)