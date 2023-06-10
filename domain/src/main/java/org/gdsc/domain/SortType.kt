package org.gdsc.domain

enum class SortType(val text: String) {
    DISTANCE("가까운 순"),
    LIKED("좋아요 순"),
    RECENCY("최신 순"),
    INIT("정렬");


    companion object {
        fun getAllText(): List<String> {
            return listOf(
                DISTANCE.text,
                LIKED.text,
                RECENCY.text
            )
        }

    }
}