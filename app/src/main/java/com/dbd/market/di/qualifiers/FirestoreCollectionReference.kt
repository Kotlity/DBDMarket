package com.dbd.market.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductsCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserCartProductsCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserAddressesCollectionReference