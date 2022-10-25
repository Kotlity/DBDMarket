package com.dbd.market.di.modules

import com.dbd.market.repositories.RegisterRepository
import com.dbd.market.repositories.RegisterRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideRegisterRepository(firebaseAuth: FirebaseAuth): RegisterRepository = RegisterRepositoryImplementation(firebaseAuth)
}