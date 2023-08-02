package com.apsl.surveyapp.core.di

import com.apsl.surveyapp.core.auth.FirebaseAuthApi
import com.apsl.surveyapp.core.auth.FirebaseAuthApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Singleton
    @Binds
    abstract fun bindFirebaseAuthApi(firebaseAuthApi: FirebaseAuthApiImpl): FirebaseAuthApi
}
