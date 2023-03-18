package org.gdsc.domain.repository

interface LoginRepository {

    fun postGoogleToken(token: String)

    fun postAppleToken(email: String, clientId: String)

}