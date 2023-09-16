package org.gdsc.data.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.gdsc.data.BuildConfig
import org.gdsc.data.network.AuthInterceptor
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
    fun provideAuthorizedApiClient(@ApplicationContext context: Context): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                baseClientBuilder
                    .addInterceptor(AuthInterceptor(context.tokenDataStore))
                    .build()
            )
            .build()
    }

    companion object {
        private const val BASE_URL = "https://api.jmt-matzip.dev/"
        private val baseClientBuilder: OkHttpClient.Builder
            get() = run {
                val clientBuilder = OkHttpClient.Builder()

                clientBuilder.apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.HEADERS
                        })

                        addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })

                        addNetworkInterceptor(StethoInterceptor())
                    }
                }
                return clientBuilder
            }
    }
}