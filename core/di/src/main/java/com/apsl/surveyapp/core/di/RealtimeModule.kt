package com.apsl.surveyapp.core.di

import com.apsl.surveyapp.core.realtime.FirebaseRealtimeDatabaseApi
import com.apsl.surveyapp.core.realtime.FirebaseRealtimeDatabaseApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RealtimeModule {

    @Singleton
    @Binds
    abstract fun bindFirebaseRealtimeDatabaseApi(firebaseRealtimeDatabaseApi: FirebaseRealtimeDatabaseApiImpl): FirebaseRealtimeDatabaseApi
}
