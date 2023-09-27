package org.gdsc.domain

enum class FoodCategory(val id: Long, val text: String, val key:String) {
    KOREAN(1L, "한식", "KOREA"),
    JAPANESE(2L, "일식", "JAPAN"),
    CHINESE(3L, "중식", "CHINA"),
    WESTERN(4L, "양식", "FOREIGN"),
    FUSION(5L, "퓨전", "FUSION"),
    CAFE(6L, "카페", "CAFE"),
    BAR(7L, "주점", "BAR"),
    ETC(8L, "기타", "ETC"),
    INIT(-1L, "(필수) 어떤 종류의 식당인가요?", "");

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

        val ALL = values().toList().dropLast(1)
        fun fromId(id: Long) = values().first { it.id == id }
        fun fromName(name: String) = values().first { it.text == name }
    }
}