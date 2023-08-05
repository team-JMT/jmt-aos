package org.gdsc.domain.model

data class Response<T>(
    val data: T,
    val message: String,
    val code: String,
)
