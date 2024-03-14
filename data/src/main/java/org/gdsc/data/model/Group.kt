package org.gdsc.data.model

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("groupId") val groupId: Int,
    @SerializedName("groupIntroduce") val groupIntroduce: String,
    @SerializedName("groupProfileImageUrl") val groupProfileImageUrl: String,
    @SerializedName("groupBackgroundImageUrl") val groupBackgroundImageUrl: String,
    @SerializedName("memberCnt") val memberCnt: Int,
    @SerializedName("restaurantCnt") val restaurantCnt: Int,
    @SerializedName("privateGroup") val privateGroup: Boolean,
    @SerializedName("isSelected") val isSelected: Boolean,
)
