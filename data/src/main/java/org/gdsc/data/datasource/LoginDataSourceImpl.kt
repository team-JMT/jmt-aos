package org.gdsc.data.datasource

import org.gdsc.domain.model.Response
import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.exception.JmtException
import org.gdsc.domain.model.request.AppleLoginRequest
import org.gdsc.domain.model.request.GoogleLoginRequest
import org.gdsc.domain.model.response.TokenResponse
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override suspend fun postSignUpWithGoogleToken(token: String): Result<Response<TokenResponse>> {
        return runCatching {
            loginAPI.postUserGoogleToken(GoogleLoginRequest(token)).body()
                ?: throw JmtException.NoneDataException()
        }.onFailure {
            throw if (it.message != null) {
                JmtException.GeneralException(requireNotNull(it.message))
            } else {
                JmtException.UnKnownException()
            }
        }

    }

    override suspend fun postAppleToken(email: String, clientId: String): TokenResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId)).data
    }

}