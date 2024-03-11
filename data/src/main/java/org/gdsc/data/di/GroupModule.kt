package org.gdsc.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.datasource.GroupDataSource
import org.gdsc.data.datasource.GroupDataSourceImpl
import org.gdsc.data.repository.GroupRepositoryImpl
import org.gdsc.domain.repository.GroupRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class GroupModule {

    @Singleton
    @Binds
    abstract fun bindGroupDataSource(groupDataSourceImpl: GroupDataSourceImpl): GroupDataSource

    @Singleton
    @Binds
    abstract fun bindGroupRepository(groupRepositoryImpl: GroupRepositoryImpl): GroupRepository
}