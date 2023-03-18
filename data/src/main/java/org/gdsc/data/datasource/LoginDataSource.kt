package org.gdsc.data.datasource

interface LoginDataSource {
    fun postGoogleToken(token: String)

    fun postAppleToken(email: String, clientId: String)
}