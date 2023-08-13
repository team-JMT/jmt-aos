package org.gdsc.data.database

data class Page(
    val totalPages: Int,
    val currentPage: Int,
    val totalElements: Int,
    val size: Int,
    val numberOfElements: Int,
    val empty: Boolean,
    val pageLast: Boolean,
    val pageFirst: Boolean,
)