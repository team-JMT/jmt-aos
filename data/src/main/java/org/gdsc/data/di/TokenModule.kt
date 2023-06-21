package org.gdsc.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.datasource.TokenManagerImpl
import org.gdsc.domain.repository.TokenManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TokenModule {

    private val TOKEN_INFO_STORAGE = "token_info_storage"

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_INFO_STORAGE)

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManagerImpl(context.dataStore)
    }

}