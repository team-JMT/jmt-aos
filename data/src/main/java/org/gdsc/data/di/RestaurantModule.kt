package org.gdsc.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.datasource.RestaurantDataSource
import org.gdsc.data.datasource.RestaurantDataSourceImpl
import org.gdsc.data.repository.RestaurantRepositoryImpl
import org.gdsc.domain.repository.RestaurantRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RestaurantModule {

    @Singleton
    @Binds
    abstract fun bindRestaurantDataSource(restaurantDataSourceImpl: RestaurantDataSourceImpl): RestaurantDataSource

    @Singleton
    @Binds
    abstract fun bindRestaurantRepository(restaurantRepositoryImpl: RestaurantRepositoryImpl): RestaurantRepository
}