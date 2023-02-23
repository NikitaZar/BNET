package ru.nikitazar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nikitazar.data.repository.DrugsRepositoryImpl
import ru.nikitazar.domain.repository.DrugsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DrugsRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDrugsRepository(imp: DrugsRepositoryImpl): DrugsRepository
}