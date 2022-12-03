package com.dbd.market.repositories.market.categories.headdress

import com.dbd.market.di.qualifiers.ProductsCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.getSomeOtherProductsFromFirebaseFirestore
import com.dbd.market.utils.getSomeProfitableProductsFromFirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class HeaddressCategoryRepositoryImplementation @Inject constructor(@ProductsCollectionReference private val productsCollectionReference: CollectionReference): HeaddressCategoryRepository {

    override suspend fun getHeaddressProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        getSomeProfitableProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE)
    }

    override suspend fun getHeaddressOtherProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        getSomeOtherProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE, pageNumber)
    }
}