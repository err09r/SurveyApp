package com.apsl.surveyapp.core.di

import com.apsl.surveyapp.core.data.auth.AuthRepository
import com.apsl.surveyapp.core.data.auth.AuthRepositoryImpl
import com.apsl.surveyapp.core.data.survey.SurveyRepository
import com.apsl.surveyapp.core.data.survey.SurveyRepositoryImpl
import com.apsl.surveyapp.core.data.user.UserRepository
import com.apsl.surveyapp.core.data.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindSurveyRepository(surveyRepository: SurveyRepositoryImpl): SurveyRepository
}
