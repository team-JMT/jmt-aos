package org.gdsc.data.datasource

import org.gdsc.domain.model.response.TokenResponse

interface LoginDataSource {
    suspend fun postGoogleToken(token: String): TokenResponse

    suspend fun postAppleToken(email: String, clientId: String): TokenResponse

}