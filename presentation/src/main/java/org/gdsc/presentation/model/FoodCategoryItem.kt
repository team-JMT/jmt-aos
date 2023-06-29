package org.gdsc.presentation.model

import org.gdsc.domain.FoodCategory

data class FoodCategoryItem(
    val categoryItem: FoodCategory,
) {
    companion object {
        val INIT = FoodCategoryItem(FoodCategory.INIT)
    }
}