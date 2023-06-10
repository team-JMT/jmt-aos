package org.gdsc.domain

enum class DrinkPossibility(val text: String) {
    POSSIBLE("가능"),
    IMPOSSIBLE("불가능"),
    UNKNOWN("알 수 없음"),
    INIT("주류 여부");

    companion object {
        fun getAllText(): List<String> {
            return listOf(
                POSSIBLE.text,
                IMPOSSIBLE.text,
                UNKNOWN.text
            )
        }
    }
}