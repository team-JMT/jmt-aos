package org.gdsc.domain.repository

import org.gdsc.domain.model.LoginResponse

interface LoginRepository {

    suspend fun postGoogleToken(token: String): LoginResponse

    suspend fun postAppleToken(email: String, clientId: String): LoginResponse

}