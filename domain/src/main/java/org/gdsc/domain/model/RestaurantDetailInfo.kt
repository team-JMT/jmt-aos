package org.gdsc.domain.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class RestaurantDetailInfo (
    @SerializedName("introduce")
    val introduce: String,
    @SerializedName("categoryId")
    val categoryId: Long,
    @SerializedName("pictures")
    val pictures: List<MultipartBody.Part>,
    @SerializedName("canDrinkLiquor")
    val canDrinkLiquor: Boolean,
    @SerializedName("goWellWithLiquor")
    val goWellWithLiquor: String,
    @SerializedName("recommendMenu")
    val recommendMenu: String,
    @SerializedName("restaurantLocationAggregateIdg")
    val restaurantLocationAggregateIdg: String,
): java.io.Serializable