package org.gdsc.presentation.model

import androidx.annotation.DrawableRes
import org.gdsc.domain.FoodCategory
import org.gdsc.presentation.R

data class FoodCategoryItem(
    val categoryItem: FoodCategory,
) {
    companion object {
        val INIT = FoodCategoryItem(FoodCategory.INIT)

    }

    @DrawableRes fun getIcon(): Int {
        return when(this.categoryItem) {
            FoodCategory.KOREAN -> R.drawable.ic_korean
            FoodCategory.JAPANESE -> R.drawable.ic_japanese
            FoodCategory.CHINESE -> R.drawable.ic_chinese
            FoodCategory.WESTERN -> R.drawable.ic_western
            FoodCategory.FUSION -> R.drawable.ic_fusion
            FoodCategory.CAFE -> R.drawable.ic_cafe
            FoodCategory.BAR -> R.drawable.ic_bar
            FoodCategory.ETC -> R.drawable.ic_etc
            else -> R.drawable.ic_etc
        }
    }

    @DrawableRes fun getMarkerIcon(): Int {
        return when(this.categoryItem) {
            FoodCategory.KOREAN -> R.drawable.ic_marker_korea
            FoodCategory.JAPANESE -> R.drawable.ic_marker_japan
            FoodCategory.CHINESE -> R.drawable.ic_marker_china
            FoodCategory.WESTERN -> R.drawable.ic_marker_foreign
            FoodCategory.FUSION -> R.drawable.ic_marker_fusion
            FoodCategory.CAFE -> R.drawable.ic_marker_cafe
            FoodCategory.BAR -> R.drawable.ic_marker_bar
            FoodCategory.ETC -> R.drawable.ic_marker_etc
            else -> R.drawable.ic_marker_etc
        }
    }
}