package org.gdsc.data.database

import org.gdsc.domain.model.Review

data class ReviewPaging(
    val page: Page,
    val reviewList: List<Review>
)