package com.nbc.curtaincall.di

import com.nbc.curtaincall.data.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.data.source.remote.api.FetchRemoteDatasource
import com.nbc.curtaincall.domain.repository.FetchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun providesFetchRepository(fetch:FetchRemoteDatasource) : FetchRepository = FetchRepositoryImpl(fetch)
}