package org.gdsc.data.repository

import org.gdsc.data.datasource.LoginDataSource
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) :
    LoginRepository {

    override fun postGoogleToken(token: String) {
        loginDataSource.postGoogleToken(token)
    }

    override fun postAppleToken(email: String, clientId: String) {
        loginDataSource.postAppleToken(email, clientId)
    }
}