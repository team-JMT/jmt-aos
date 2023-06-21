package org.gdsc.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.gdsc.domain.Empty
import org.gdsc.domain.model.TokenResponse
import org.gdsc.domain.repository.TokenManager
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): TokenManager {

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

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        private val ACCESS_TOKEN_EXPIRES_IN = longPreferencesKey("accessTokenExpiresIn")
        private val GRANT_TYPE = stringPreferencesKey("grantType")
        private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }

}