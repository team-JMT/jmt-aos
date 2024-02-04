package org.gdsc.data.database

import com.google.gson.annotations.SerializedName
import org.gdsc.data.model.RegisteredRestaurantResponse

data class RegisteredRestaurantPaging(
    @SerializedName("restaurants")
    val restaurants: List<RegisteredRestaurantResponse>,
    @SerializedName("page")
    val page: Page,
)