package com.apsl.surveyapp.core.di

import com.apsl.surveyapp.core.storage.FirebaseStorageApi
import com.apsl.surveyapp.core.storage.FirebaseStorageApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Singleton
    @Binds
    abstract fun bindFirebaseStorageApi(firebaseStorageApi: FirebaseStorageApiImpl): FirebaseStorageApi
}
