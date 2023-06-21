package org.gdsc.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.gdsc.data.network.AuthInterceptor
import org.gdsc.domain.repository.TokenManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @NormalClient
    @Provides
    @Singleton
    fun provideApiClient(): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                baseClientBuilder.build()
            ).build()
    }

    @AuthClient
    @Provides
    @Singleton
    fun provideAuthorizedApiClient(tokenManager: TokenManager): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                baseClientBuilder
                    .addInterceptor(AuthInterceptor(tokenManager))
                    .build()
            )
            .build()
    }

    companion object {
        private const val BASE_URL = "http://13.209.81.126:8080/"
        private val baseClientBuilder: OkHttpClient.Builder
            get() = run {
                val clientBuilder = OkHttpClient.Builder()
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                return clientBuilder.addInterceptor(loggingInterceptor)
            }

    }
}