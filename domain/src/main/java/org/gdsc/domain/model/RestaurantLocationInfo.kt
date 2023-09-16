package org.gdsc.domain.model

import com.google.gson.annotations.SerializedName

data class RestaurantLocationInfo(
    @SerializedName("addressName")
    val addressName: String,
    @SerializedName("categoryGroupCode")
    val categoryGroupCode: String,
    @SerializedName("categoryGroupName")
    val categoryGroupName: String,
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("distance")
    val distance: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("placeUrl")
    val placeUrl: String,
    @SerializedName("roadAddressName")
    val roadAddressName: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String
): java.io.Serializable