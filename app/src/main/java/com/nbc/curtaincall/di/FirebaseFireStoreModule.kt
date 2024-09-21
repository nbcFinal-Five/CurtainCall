package com.nbc.curtaincall.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseFireStoreModule {
    @Provides
    @Singleton
    fun providesFireStore(): FirebaseFirestore = Firebase.firestore
}