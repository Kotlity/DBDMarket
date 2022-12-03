package com.dbd.market.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductsCollectionReference

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserCartProductsCollectionReference