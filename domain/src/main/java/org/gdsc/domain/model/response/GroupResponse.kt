package org.gdsc.domain.model.response

data class GroupResponse(
    val groupId: Int,
    val groupName: String,
    val groupIntroduce: String,
    val groupProfileImageUrl: String,
    val groupBackgroundImageUrl: String,
    val memberCnt: Int,
    val restaurantCnt: Int,
    val privateGroup: Boolean,
    val isSelected: Boolean,
)