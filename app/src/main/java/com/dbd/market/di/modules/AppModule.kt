package com.dbd.market.di.modules

import com.dbd.market.di.qualifiers.ProductsCollectionReference
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.repositories.introduction.login.LoginRepository
import com.dbd.market.repositories.introduction.login.LoginRepositoryImplementation
import com.dbd.market.repositories.introduction.register.RegisterRepository
import com.dbd.market.repositories.introduction.register.RegisterRepositoryImplementation
import com.dbd.market.repositories.market.cart.CartProductsRepository
import com.dbd.market.repositories.market.cart.CartProductsRepositoryImplementation
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
import com.dbd.market.repositories.market.product_description.ProductDescriptionRepository
import com.dbd.market.repositories.market.product_description.ProductDescriptionRepositoryImplementation
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_CART_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
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
    fun provideUserUid() = FirebaseAuth.getInstance().currentUser?.uid

    @UserCartProductsCollectionReference
    @Provides
    @Singleton
    fun provideUserCartProductsCollectionReference(userUid: String?) = userUid?.let { Firebase.firestore.collection(FIREBASE_FIRESTORE_USER_COLLECTION).document(it).collection(FIREBASE_FIRESTORE_CART_PRODUCTS_COLLECTION) }

    @Provides
    @Singleton
    fun provideCartProductsRepository(@UserCartProductsCollectionReference cartProductsCollectionReference: CollectionReference?): CartProductsRepository = CartProductsRepositoryImplementation(cartProductsCollectionReference)

    @Provides
    @Singleton
    fun provideProductDescriptionRepository(@UserCartProductsCollectionReference cartProductsCollectionReference: CollectionReference?): ProductDescriptionRepository = ProductDescriptionRepositoryImplementation(cartProductsCollectionReference)

    @ProductsCollectionReference
    @Provides
    @Singleton
    fun provideProductsCollectionReference() = Firebase.firestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)

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
    fun provideSuitsCategoryRepository(@ProductsCollectionReference productsCollectionReference: CollectionReference): SuitsCategoryRepository = SuitsCategoryRepositoryImplementation(productsCollectionReference)

    @Provides
    @Singleton
    fun provideWeaponsCategoryRepository(@ProductsCollectionReference productsCollectionReference: CollectionReference): WeaponsCategoryRepository = WeaponsCategoryRepositoryImplementation(productsCollectionReference)

    @Provides
    @Singleton
    fun provideHeaddressCategoryRepository(@ProductsCollectionReference productsCollectionReference: CollectionReference): HeaddressCategoryRepository = HeaddressCategoryRepositoryImplementation(productsCollectionReference)

    @Provides
    @Singleton
    fun provideTorsoCategoryRepository(@ProductsCollectionReference productsCollectionReference: CollectionReference): TorsoCategoryRepository = TorsoCategoryRepositoryImplementation(productsCollectionReference)

    @Provides
    @Singleton
    fun provideLegsCategoryRepository(@ProductsCollectionReference productsCollectionReference: CollectionReference): LegsCategoryRepository = LegsCategoryRepositoryImplementation(productsCollectionReference)
}