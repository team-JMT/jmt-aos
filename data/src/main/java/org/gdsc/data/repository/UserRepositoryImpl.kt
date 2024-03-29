package org.gdsc.data.repository

import okhttp3.MultipartBody
import org.gdsc.data.datasource.UserDataSource
import org.gdsc.domain.model.Response
import org.gdsc.domain.model.request.NicknameRequest
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postNickname(nicknameRequest: NicknameRequest): NicknameResponse {
        return userDataSource.postNickname(nicknameRequest)
    }

    override suspend fun postProfileImg(file: MultipartBody.Part): Response<String> {
        return userDataSource.postProfileImg(file)
    }

    override suspend fun postDefaultProfileImg(): Response<String> {
        return userDataSource.postDefaultProfileImg()
    }

    override suspend fun checkDuplicatedNickname(nickname: String): Boolean {
        return userDataSource.checkDuplicatedNickname(nickname)
    }

    override suspend fun getUserInfo(): UserInfo {
        return userDataSource.getUserInfo()
    }

    override suspend fun getOtherUserInfo(id: Int): UserInfo {
        return userDataSource.getOtherUserInfo(id)
    }

    override suspend fun postUserLogout(refreshToken: String): String {
        return userDataSource.postUserLogout(refreshToken)
    }

    override suspend fun postUserSignout(): String {
        return userDataSource.postUserSignout()
    }

    override suspend fun getSearchedKeywords(): List<String> {
        return userDataSource.getSearchedKeywords()
    }

    override suspend fun updateSearchedKeyword(newKeyword: String): List<String> {
        return userDataSource.updateSearchedKeyword(newKeyword)
    }

    override suspend fun deleteSearchedKeyword(targetKeyword: String): List<String> {
        return userDataSource.deleteSearchedKeyword(targetKeyword)
    }

    override suspend fun initSearchedKeyword(): List<String> {
        return userDataSource.initSearchedKeyword()
    }

}