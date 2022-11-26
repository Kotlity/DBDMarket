package com.dbd.market.repositories.market.categories.torso

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_TORSO_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.getSomeOtherProductsFromFirebaseFirestore
import com.dbd.market.utils.getSomeProfitableProductsFromFirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class TorsoCategoryRepositoryImplementation @Inject constructor(private val productsCollectionReference: CollectionReference): TorsoCategoryRepository {

    override suspend fun getTorsoProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        getSomeProfitableProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_TORSO_PRODUCTS_CATEGORY_FIELD_VALUE)
    }

    override suspend fun getTorsoOtherProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        getSomeOtherProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_TORSO_PRODUCTS_CATEGORY_FIELD_VALUE, pageNumber)
    }
}