package com.dbd.market.di.modules

import com.dbd.market.repositories.introduction.login.LoginRepository
import com.dbd.market.repositories.introduction.login.LoginRepositoryImplementation
import com.dbd.market.repositories.introduction.register.RegisterRepository
import com.dbd.market.repositories.introduction.register.RegisterRepositoryImplementation
import com.dbd.market.repositories.market.categories.headdress.HeaddressCategoryRepository
import com.dbd.market.repositories.market.categories.headdress.HeaddressCategoryRepositoryImplementation
import com.dbd.market.repositories.market.categories.legs.LegsCategoryRepository
import com.dbd.market.repositories.market.categories.legs.LegsCategoryRepositoryImplementation
import com.dbd.market.repositories.market.categories.main_category.MainCategoryRepository
import com.dbd.market.repositories.market.categories.main_category.MainCategoryRepositoryImplementation
import com.dbd.market.repositories.market.categories.suits.SuitsCategoryRepository
import com.dbd.market.repositories.market.categories.suits.SuitsCategoryRepositoryImplementation
import com.dbd.market.repositories.market.categories.torso.TorsoCategoryRepository
import com.dbd.market.repositories.market.categories.torso.TorsoCategoryRepositoryImplementation
import com.dbd.market.repositories.market.categories.weapons.WeaponsCategoryRepository
import com.dbd.market.repositories.market.categories.weapons.WeaponsCategoryRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

    @Provides
    @Singleton
    fun provideFirebaseFirestoreStorage() = Firebase.storage.reference

    @Provides
    @Singleton
    fun provideMainCategoryRepository(firebaseFirestore: FirebaseFirestore): MainCategoryRepository = MainCategoryRepositoryImplementation(firebaseFirestore)

    @Provides
    @Singleton
    fun provideSuitsCategoryRepository(firebaseFirestore: FirebaseFirestore): SuitsCategoryRepository = SuitsCategoryRepositoryImplementation(firebaseFirestore)

    @Provides
    @Singleton
    fun provideWeaponsCategoryRepository(firebaseFirestore: FirebaseFirestore): WeaponsCategoryRepository = WeaponsCategoryRepositoryImplementation(firebaseFirestore)

    @Provides
    @Singleton
    fun provideHeaddressCategoryRepository(firebaseFirestore: FirebaseFirestore): HeaddressCategoryRepository = HeaddressCategoryRepositoryImplementation(firebaseFirestore)

    @Provides
    @Singleton
    fun provideTorsoCategoryRepository(firebaseFirestore: FirebaseFirestore): TorsoCategoryRepository = TorsoCategoryRepositoryImplementation(firebaseFirestore)

    @Provides
    @Singleton
    fun provideLegsCategoryRepository(firebaseFirestore: FirebaseFirestore): LegsCategoryRepository = LegsCategoryRepositoryImplementation(firebaseFirestore)
}