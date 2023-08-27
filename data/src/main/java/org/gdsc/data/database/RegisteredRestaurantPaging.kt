package org.gdsc.data.database

import org.gdsc.data.model.RegisteredRestaurantResponse

data class RegisteredRestaurantPaging(
    val restaurants: List<RegisteredRestaurantResponse>,
    val page: Page,
)