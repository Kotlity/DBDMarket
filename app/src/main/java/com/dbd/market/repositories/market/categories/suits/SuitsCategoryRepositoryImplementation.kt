package com.dbd.market.repositories.market.categories.suits

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.getSomeOtherProductsFromFirebaseFirestore
import com.dbd.market.utils.getSomeProfitableProductsFromFirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class SuitsCategoryRepositoryImplementation @Inject constructor(private val productsCollectionReference: CollectionReference): SuitsCategoryRepository {

    override suspend fun getSuitsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        getSomeProfitableProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE)
    }

    override suspend fun getSuitsOtherCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        getSomeOtherProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE, pageNumber)
    }
}