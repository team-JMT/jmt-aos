package org.gdsc.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.datasource.LoginDataSource
import org.gdsc.data.datasource.LoginDataSourceImpl
import org.gdsc.domain.repository.LoginRepository
import org.gdsc.data.repository.LoginRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Singleton
    @Binds
    abstract fun bindLoginDataSource(loginDataSourceImpl: LoginDataSourceImpl): LoginDataSource

    @Singleton
    @Binds
    abstract fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

}