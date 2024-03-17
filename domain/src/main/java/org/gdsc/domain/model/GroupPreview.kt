package org.gdsc.domain.model

data class GroupPreview(
    val groupId: Int,
    val groupName: String,
    val groupProfileImageUrl: String,
    val groupIntroduce: String,
    val memberCnt: Int,
    val restaurantCnt: Int
)