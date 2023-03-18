package org.gdsc.domain.model

data class LoginResponse(
    val code: String,
    val `data`: Data,
    val message: String
)

data class Data(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)