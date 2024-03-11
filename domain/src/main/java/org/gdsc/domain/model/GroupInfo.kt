package org.gdsc.domain.model

data class GroupInfo(
    val groupBackgroundImageUrl: String,
    val groupId: Int,
    val groupIntroduce: String,
    val groupName: String,
    val groupProfileImageUrl: String,
    val isSelected: Boolean,
    val memberCnt: Int,
    val privateGroup: Boolean,
    val restaurantCnt: Int
)