package org.gdsc.data.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import org.gdsc.domain.repository.TokenRepository
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
    private val tokenRepository: TokenRepository
) : CoroutineInterceptor {

    override suspend fun interceptSuspend(chain: Interceptor.Chain): Response {
        val token = "Bearer ${tokenRepository.getAccessToken()}"
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}