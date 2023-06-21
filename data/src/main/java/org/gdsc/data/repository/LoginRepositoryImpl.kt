package org.gdsc.data.repository

import org.gdsc.data.datasource.LoginDataSource
import org.gdsc.domain.model.TokenResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) :
    LoginRepository {

    override suspend fun postGoogleToken(token: String): TokenResponse {
        return loginDataSource.postGoogleToken(token)
    }

    override suspend fun postAppleToken(email: String, clientId: String): TokenResponse {
        return loginDataSource.postAppleToken(email, clientId)
    }

}