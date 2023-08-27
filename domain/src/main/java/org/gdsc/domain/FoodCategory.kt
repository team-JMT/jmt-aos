package org.gdsc.domain

enum class FoodCategory(val id: Long, val text: String) {
    KOREAN(1L, "한식"),
    JAPANESE(2L, "일식"),
    CHINESE(3L, "중식"),
    WESTERN(4L, "양식"),
    CAFE(5L, "카페"),
    BAR(6L, "주점"),
    ETC(7L, "기타"),
    INIT(-1L, "(필수) 어떤 종류의 식당인가요?");

    companion object {
        fun getAllText(): List<String> {
            return listOf(
                KOREAN.text,
                JAPANESE.text,
                CHINESE.text,
                WESTERN.text,
                CAFE.text,
                BAR.text,
                ETC.text
            )
        }

        val ALL = values().toList().dropLast(1)
        fun fromId(id: Long) = values().first { it.id == id }
        fun fromName(name: String) = values().first { it.text == name }
    }
}