package org.gdsc.domain.model

data class Review(
    val recommendRestaurantId: Int,
    val reviewContent: String,
    val reviewId: Int,
    val reviewImages: List<String>,
    val reviewerImageUrl: String,
    val userName: String
)