package org.gdsc.data.datasource

import org.gdsc.domain.model.LoginResponse

interface LoginDataSource {
    suspend fun postGoogleToken(token: String): LoginResponse

    suspend fun postAppleToken(email: String, clientId: String): LoginResponse

}