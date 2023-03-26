package org.gdsc.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gdsc.data.datasource.GalleryDataSource
import org.gdsc.data.datasource.GalleryDataSourceImpl
import org.gdsc.data.repository.GalleryRepositoryImpl
import org.gdsc.domain.repository.GalleryRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryModule {
    @Singleton
    @Binds
    abstract fun bindGalleryDataSource(galleryDataSourceImpl: GalleryDataSourceImpl): GalleryDataSource

    @Singleton
    @Binds
    abstract fun bindGalleryRepository(galleryRepositoryImpl: GalleryRepositoryImpl): GalleryRepository
}