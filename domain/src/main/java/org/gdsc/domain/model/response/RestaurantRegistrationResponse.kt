package org.gdsc.domain.model.response

data class RestaurantRegistrationResponse(
    val restaurantLocationAggregateId: String,
    val recommendRestaurantAggregateId: String
)