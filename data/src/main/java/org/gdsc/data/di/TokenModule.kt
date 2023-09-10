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
import org.gdsc.data.datasource.TokenDataSource
import org.gdsc.data.datasource.TokenDataSourceImpl
import org.gdsc.data.network.TokenAPI
import org.gdsc.data.repository.TokenRepositoryImpl
import org.gdsc.domain.repository.TokenRepository
import javax.inject.Singleton

val TOKEN_INFO_STORAGE = "token_info_storage"
val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_INFO_STORAGE)

@Module
@InstallIn(SingletonComponent::class)
class TokenModule {

    @Provides
    @Singleton
    fun provideTokenDataSource(tokenAPI: TokenAPI): TokenDataSource {
        return TokenDataSourceImpl(tokenAPI)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context, tokenDataSource: TokenDataSource): TokenRepository {
        return TokenRepositoryImpl(context.tokenDataStore, tokenDataSource)
    }

}