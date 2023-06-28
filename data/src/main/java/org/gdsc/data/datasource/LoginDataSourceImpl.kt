package org.gdsc.data.datasource

import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.model.request.AppleLoginRequest
import org.gdsc.domain.model.request.GoogleLoginRequest
import org.gdsc.domain.model.response.TokenResponse
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override suspend fun postSignUpWithGoogleToken(token: String): TokenResponse {
        return loginAPI.postUserGoogleToken(GoogleLoginRequest(token)).data
    }

    override suspend fun postAppleToken(email: String, clientId: String): TokenResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId)).data
    }

}