package com.dbd.market.repositories.market.categories.suits

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SuitsCategoryRepositoryImplementation @Inject constructor(private val firebaseFirestore: FirebaseFirestore): SuitsCategoryRepository {

    override suspend fun getSuitsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val suitsProfitableQuerySnapshot = firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE)
                    .whereNotEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                    .get().await()
                if (!suitsProfitableQuerySnapshot.isEmpty) onSuccess(suitsProfitableQuerySnapshot)
            } catch (e: Exception) { onFailure(e) }
        }
    }

    override suspend fun getSuitsOtherCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val suitsOtherQuerySnapshot = firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                    .limit(pageNumber * 4)
                    .get().await()
                if (!suitsOtherQuerySnapshot.isEmpty) onSuccess(suitsOtherQuerySnapshot)
            } catch (e: Exception) { onFailure(e) }
        }
    }
}