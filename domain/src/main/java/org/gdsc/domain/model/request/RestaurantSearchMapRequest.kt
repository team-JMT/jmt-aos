package org.gdsc.domain.model.request

import com.google.gson.annotations.SerializedName
import org.gdsc.domain.model.Filter
import org.gdsc.domain.model.Location

data class RestaurantSearchMapRequest(
    @SerializedName("filter")
    val filter: Filter,
    @SerializedName("userLocation")
    val userLocation: Location? = null,
    @SerializedName("startLocation")
    val startLocation: Location? = null,
    @SerializedName("endLocation")
    val endLocation: Location? = null,
)
