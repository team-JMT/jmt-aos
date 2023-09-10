package org.gdsc.domain.repository

import org.gdsc.domain.model.response.TokenResponse

interface TokenRepository {

    suspend fun saveTokenInfo(tokenResponse: TokenResponse)

    suspend fun getAccessToken(): String

    suspend fun getAccessTokenExpiresIn(): Long

    suspend fun getGrantType(): String

    suspend fun getRefreshToken(): String

    suspend fun clearTokenInfo()
    
}