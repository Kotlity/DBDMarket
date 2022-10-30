package com.dbd.market.di.modules

import com.dbd.market.repositories.login.LoginRepository
import com.dbd.market.repositories.login.LoginRepositoryImplementation
import com.dbd.market.repositories.register.RegisterRepository
import com.dbd.market.repositories.register.RegisterRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    fun provideRegisterRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore): RegisterRepository = RegisterRepositoryImplementation(firebaseAuth, firebaseFirestore)

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideLoginRepository(firebaseAuth: FirebaseAuth): LoginRepository = LoginRepositoryImplementation(firebaseAuth)
}