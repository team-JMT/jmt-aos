package org.gdsc.data.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.gdsc.domain.Empty
import java.io.IOException
import javax.inject.Inject

// reference: https://github.com/square/okhttp/issues/7164
@Suppress("potential danger of runBlocking")
interface CoroutineInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            try {
                interceptSuspend(chain)
            } catch (ce: CancellationException) {
                throw IOException(ce)
            }
        }
    }

    suspend fun interceptSuspend(chain: Interceptor.Chain): Response
}

class AuthInterceptor @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : CoroutineInterceptor {

    override suspend fun interceptSuspend(chain: Interceptor.Chain): Response {

        val accessToken = dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("accessToken")]
        }.first() ?: String.Empty

        val token = "Bearer $accessToken"
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}