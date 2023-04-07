package org.gdsc.data.model

data class Response<T>(
    val data: T,
    val message: String,
    val codes: String
)