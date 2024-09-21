package com.nbc.curtaincall.di

import com.google.firebase.firestore.FirebaseFirestore
import com.nbc.curtaincall.data.repository.impl.BookmarkRepositoryImpl
import com.nbc.curtaincall.domain.repository.BookmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BookmarkRepositoryModule {
    @Provides
    @ViewModelScoped
    fun providesBookmarkRepository(firestore: FirebaseFirestore): BookmarkRepository =
        BookmarkRepositoryImpl(firestore)
}