package org.gdsc.data.repository

import org.gdsc.data.datasource.LoginDataSource
import org.gdsc.domain.model.LoginResponse
import org.gdsc.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) :
    LoginRepository {

    override suspend fun postGoogleToken(token: String): LoginResponse {
        return loginDataSource.postGoogleToken(token)
    }

    override suspend fun postAppleToken(email: String, clientId: String): LoginResponse {
        return loginDataSource.postAppleToken(email, clientId)
    }

    override fun getGalleryImage(): ArrayList<String> {
        return loginDataSource.getGalleryImage()
    }
}