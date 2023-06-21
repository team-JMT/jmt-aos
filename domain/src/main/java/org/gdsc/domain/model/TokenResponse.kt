package org.gdsc.domain.model

data class TokenResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String
)