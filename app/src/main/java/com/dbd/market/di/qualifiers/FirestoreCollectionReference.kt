package com.dbd.market.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductsCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserCartProductsCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserAddressesCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserOrderCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseStorageReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirebaseStorageReferenceUserImages