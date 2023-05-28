package org.gdsc.domain

enum class FoodCategory(val text: String) {
    KOREAN("한식"),
    JAPANESE("일식"),
    CHINESE("중식"),
    WESTERN("양식"),
    FUSION("퓨전"),
    CAFE("카페"),
    BAR("주점"),
    ETC("기타"),
    INIT("종류");

    companion object {
        fun getAllText(): List<String> {
            return listOf(
                KOREAN.text,
                JAPANESE.text,
                CHINESE.text,
                WESTERN.text,
                FUSION.text,
                CAFE.text,
                BAR.text,
                ETC.text
            )
        }
    }
}