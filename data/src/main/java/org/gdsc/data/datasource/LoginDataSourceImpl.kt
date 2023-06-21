package org.gdsc.data.datasource

import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.model.AppleLoginRequest
import org.gdsc.domain.model.GoogleLoginRequest
import org.gdsc.domain.model.TokenResponse
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override suspend fun postGoogleToken(token: String): TokenResponse {
        return loginAPI.postUserGoogleToken(GoogleLoginRequest(token)).data
    }

    override suspend fun postAppleToken(email: String, clientId: String): TokenResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId)).data
    }

}