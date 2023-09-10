package org.gdsc.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.gdsc.domain.Empty
import org.gdsc.domain.model.response.TokenResponse
import org.gdsc.domain.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): TokenRepository {


    override suspend fun saveTokenInfo(tokenResponse: TokenResponse) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = tokenResponse.accessToken
            preferences[ACCESS_TOKEN_EXPIRES_IN] = tokenResponse.accessTokenExpiresIn
            preferences[GRANT_TYPE] = tokenResponse.grantType
            preferences[REFRESH_TOKEN] = tokenResponse.refreshToken
        }
    }

    override suspend fun getAccessToken(): String {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }.first() ?: String.Empty
    }

    override suspend fun getAccessTokenExpiresIn(): Long {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRES_IN]
        }.first() ?: -1L
    }

    override suspend fun getGrantType(): String {
        return dataStore.data.map { preferences ->
            preferences[GRANT_TYPE]
        }.first() ?: String.Empty
    }

    override suspend fun getRefreshToken(): String {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }.first() ?: String.Empty
    }

    override suspend fun clearTokenInfo() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(ACCESS_TOKEN_EXPIRES_IN)
            preferences.remove(GRANT_TYPE)
            preferences.remove(REFRESH_TOKEN)
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        private val ACCESS_TOKEN_EXPIRES_IN = longPreferencesKey("accessTokenExpiresIn")
        private val GRANT_TYPE = stringPreferencesKey("grantType")
        private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

}