package org.gdsc.data.datasource

import org.gdsc.data.model.Response
import org.gdsc.data.network.TokenAPI
import org.gdsc.domain.model.request.RefreshTokenRequest
import org.gdsc.domain.model.response.TokenResponse
import javax.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val tokenAPI: TokenAPI
): TokenDataSource {
    override suspend fun postRefreshToken(refreshToken: String): Response<TokenResponse> {
        return tokenAPI.refreshToken(RefreshTokenRequest(refreshToken))
    }
}