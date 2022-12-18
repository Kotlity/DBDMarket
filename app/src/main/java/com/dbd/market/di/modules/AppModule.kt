package com.dbd.market.di.modules

import com.dbd.market.di.qualifiers.ProductsCollectionReference
import com.dbd.market.di.qualifiers.UserAddressesCollectionReference
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.di.qualifiers.UserCollectionReference
import com.dbd.market.helpers.operations.UserAddressesFirestoreOperations
import com.dbd.market.helpers.operations.UserCartProductsFirestoreOperations
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
import com.dbd.market.repositories.market.setup_order.SetupOrderRepository
import com.dbd.market.repositories.market.setup_order.SetupOrderRepositoryImplementation
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_ADDRESSES_COLLECTION
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

    @UserCollectionReference
    @Provides
    @Singleton
    fun provideUserCollectionReference() = Firebase.firestore.collection(FIREBASE_FIRESTORE_USER_COLLECTION)

    @Provides
    @Singleton
    fun provideCartProductsFirestoreOperations(@UserCartProductsCollectionReference cartProductsCollectionReference: CollectionReference?, firebaseFirestore: FirebaseFirestore) = UserCartProductsFirestoreOperations(cartProductsCollectionReference, firebaseFirestore)

    @Provides
    @Singleton
    fun provideAddressesFirestoreOperations(@UserAddressesCollectionReference userAddressesCollectionReference: CollectionReference?) = UserAddressesFirestoreOperations(userAddressesCollectionReference)

    @UserCartProductsCollectionReference
    @Provides
    @Singleton
    fun provideUserCartProductsCollectionReference(userUid: String?, @UserCollectionReference userCollectionReference: CollectionReference) = userUid?.let { userCollectionReference.document(it).collection(FIREBASE_FIRESTORE_CART_PRODUCTS_COLLECTION) }

    @UserAddressesCollectionReference
    @Provides
    @Singleton
    fun provideUserAddressesCollectionReference(userUid: String?, @UserCollectionReference userCollectionReference: CollectionReference) = userUid?.let { userCollectionReference.document(it).collection(FIREBASE_FIRESTORE_ADDRESSES_COLLECTION) }


    @Provides
    @Singleton
    fun provideCartProductsRepository(@UserCartProductsCollectionReference cartProductsCollectionReference: CollectionReference?): CartProductsRepository = CartProductsRepositoryImplementation(cartProductsCollectionReference)

    @Provides
    @Singleton
    fun provideProductDescriptionRepository(@UserCartProductsCollectionReference cartProductsCollectionReference: CollectionReference?, userCartProductsFirestoreOperations: UserCartProductsFirestoreOperations): ProductDescriptionRepository = ProductDescriptionRepositoryImplementation(cartProductsCollectionReference, userCartProductsFirestoreOperations)

    @Provides
    @Singleton
    fun provideSetupOrderRepository(@UserAddressesCollectionReference userAddressesCollectionReference: CollectionReference?): SetupOrderRepository = SetupOrderRepositoryImplementation(userAddressesCollectionReference)

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