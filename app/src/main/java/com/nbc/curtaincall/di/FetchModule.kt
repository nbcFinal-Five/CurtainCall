package com.nbc.curtaincall.di

import com.nbc.curtaincall.data.source.remote.api.FetchRemoteDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FetchModule {
    @Provides
    @Singleton
    fun providesFetch(retrofit: Retrofit): FetchRemoteDatasource =
        retrofit.create(FetchRemoteDatasource::class.java)
}