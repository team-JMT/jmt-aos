package org.gdsc.data.datasource

import org.gdsc.domain.model.Response
import org.gdsc.domain.model.response.TokenResponse

interface LoginDataSource {
    suspend fun postSignUpWithGoogleToken(token: String): Result<Response<TokenResponse>>

    suspend fun postAppleToken(email: String, clientId: String): TokenResponse

}