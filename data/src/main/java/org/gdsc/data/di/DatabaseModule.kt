package org.gdsc.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.database.RestaurantDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RestaurantDatabase {
        return RestaurantDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideRestaurantDao(database: RestaurantDatabase) = database.restaurantDao()
}