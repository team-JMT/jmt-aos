package org.gdsc.domain

enum class SortType(val text: String, val key: String) {
    DISTANCE("가까운 순", "distance,asc"),
    LIKED("좋아요 순", ""),
    RECENCY("최신 순", "");


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