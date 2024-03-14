package org.gdsc.domain.model.request

import com.google.gson.annotations.SerializedName
import org.gdsc.domain.model.Filter
import org.gdsc.domain.model.Location

data class RestaurantSearchRequest(
    @SerializedName("filter")
    val filter: Filter? = null,
    @SerializedName("userLocation")
    val userLocation: Location? = null,
    @SerializedName("startLocation")
    val startLocation: Location? = null,
    @SerializedName("endLocation")
    val endLocation: Location? = null,
    @SerializedName("groupId")
    val groupId: Int? = null,
    @SerializedName("keyword")
    val keyword: String? = null,
)
