package org.gdsc.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.gdsc.data.network.LoginAPI
import org.gdsc.domain.model.AppleLoginRequest
import org.gdsc.domain.model.GoogleLoginRequest
import org.gdsc.domain.model.LoginResponse
import java.io.File
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val loginAPI: LoginAPI
) : LoginDataSource {
    override suspend fun postGoogleToken(token: String): LoginResponse {
        return loginAPI.postUserGoogleToken(GoogleLoginRequest(token))
    }

    override suspend fun postAppleToken(email: String, clientId: String): LoginResponse {
        return loginAPI.postUserAppleToken(AppleLoginRequest(email, clientId))
    }

}