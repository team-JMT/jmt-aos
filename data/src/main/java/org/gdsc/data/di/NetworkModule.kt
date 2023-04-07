package org.gdsc.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.gdsc.data.network.LoginAPI
import org.gdsc.data.network.RestaurantAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApiClient(): Retrofit {

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)

        return Retrofit
            .Builder()
            .baseUrl("http://34.64.147.86:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build()).build()
    }

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginAPI {
        return retrofit.create(LoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRestaurantApi(retrofit: Retrofit): RestaurantAPI {
        return retrofit.create(RestaurantAPI::class.java)
    }

}