package org.gdsc.domain.repository

import org.gdsc.domain.model.response.TokenResponse

interface LoginRepository {

    suspend fun postGoogleToken(token: String): TokenResponse

    suspend fun postAppleToken(email: String, clientId: String): TokenResponse

}