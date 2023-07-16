package org.gdsc.domain.model.response

data class TokenResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val grantType: String,
    val refreshToken: String,
    val userLoginAction: String,
)

enum class UserLoginAction(val value: String) {
    SIGN_UP("SIGN_UP"),
    LOG_IN("LOG_IN")
}