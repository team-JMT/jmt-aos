package org.gdsc.data.datasource

import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.model.AppleLoginRequest
import org.gdsc.domain.model.GoogleLoginRequest
import org.gdsc.domain.model.LoginResponse
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override suspend fun postGoogleToken(token: String): LoginResponse {
        return loginAPI.postUserGoogleToken(GoogleLoginRequest(token))
    }

    override suspend fun postAppleToken(email: String, clientId: String): LoginResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId))
    }
}