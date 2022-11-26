package com.dbd.market.utils

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

suspend fun getSomeProfitableProductsFromFirebaseFirestore(collectionReference: CollectionReference, onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, productsCategory: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val someProfitableProductsQuerySnapshot = collectionReference
                .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, productsCategory)
                .whereNotEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                .get().await()
            if (!someProfitableProductsQuerySnapshot.isEmpty) onSuccess(someProfitableProductsQuerySnapshot)
        } catch (e: Exception) { onFailure(e) }
    }
}

suspend fun getSomeOtherProductsFromFirebaseFirestore(collectionReference: CollectionReference, onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, productsCategory: String, pageNumber: Long) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val someOtherProductsQuerySnapshot = collectionReference
                .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, productsCategory)
                .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                .limit(pageNumber * 4)
                .get().await()
            if (!someOtherProductsQuerySnapshot.isEmpty) onSuccess(someOtherProductsQuerySnapshot)
        } catch (e: Exception) { onFailure(e) }
    }
}