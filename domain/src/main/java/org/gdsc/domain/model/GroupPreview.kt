package org.gdsc.domain.model

data class GroupPreview(
    val groupId: Int,
    val groupIntroduce: String,
    val groupName: String,
    val memberCnt: Int,
    val restaurantCnt: Int
)