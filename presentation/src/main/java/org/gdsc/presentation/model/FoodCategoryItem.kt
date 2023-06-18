package org.gdsc.presentation.model

data class FoodCategoryItem(
    val name: String,
) {
    companion object {
        val INIT = FoodCategoryItem("(필수) 어떤 종류의 식당인가요?")
    }
}