package org.gdsc.domain.model.request

import org.gdsc.domain.model.Filter
import org.gdsc.domain.model.Location

data class RestaurantSearchMapRequest(
    val userLocation: Location,
    val filter: Filter,
)
