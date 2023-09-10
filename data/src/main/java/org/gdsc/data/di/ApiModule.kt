package org.gdsc.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.network.LoginAPI
import org.gdsc.data.network.RestaurantAPI
import org.gdsc.data.network.TokenAPI
import org.gdsc.data.network.UserAPI
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideLoginApi(@NormalClient retrofit: Retrofit): LoginAPI {
        return retrofit.create(LoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRestaurantApi(@AuthClient retrofit: Retrofit): RestaurantAPI {
        return retrofit.create(RestaurantAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(@AuthClient retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(@AuthClient retrofit: Retrofit): TokenAPI {
        return retrofit.create(TokenAPI::class.java)
    }

}