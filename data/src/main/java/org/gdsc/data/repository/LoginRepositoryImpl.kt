package org.gdsc.data.repository

import org.gdsc.data.datasource.LoginDataSource
import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) :
    LoginRepository {

    override suspend fun postSignUpWithGoogleToken(token: String): Result<TokenResponse> {
        return kotlin.runCatching {
            loginDataSource.postSignUpWithGoogleToken(token).getOrThrow().data
        }
    }

    override suspend fun postAppleToken(email: String, clientId: String): TokenResponse {
        return loginDataSource.postAppleToken(email, clientId)
    }

}