package org.gdsc.data.datasource

import org.gdsc.data.model.Response
import org.gdsc.domain.model.response.TokenResponse

interface TokenDataSource {

    suspend fun postRefreshToken(refreshToken: String): Response<TokenResponse>
}