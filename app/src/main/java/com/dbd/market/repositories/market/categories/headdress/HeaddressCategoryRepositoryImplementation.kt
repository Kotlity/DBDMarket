package com.dbd.market.repositories.market.categories.headdress

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HeaddressCategoryRepositoryImplementation @Inject constructor(private val firebaseFirestore: FirebaseFirestore): HeaddressCategoryRepository {

    override suspend fun getHeaddressProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val headdressProfitableQuerySnapshot = firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE)
                    .whereNotEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                    .get().await()
                if (!headdressProfitableQuerySnapshot.isEmpty) onSuccess(headdressProfitableQuerySnapshot)
            } catch (e: Exception) { onFailure(e) }
        }
    }

    override suspend fun getHeaddressOtherProductsFromFirebaseFirestore(onSucces: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val headdressOtherQuerySnapshot = firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                    .limit(pageNumber * 4)
                    .get().await()
                if (!headdressOtherQuerySnapshot.isEmpty) onSucces(headdressOtherQuerySnapshot)
            } catch (e: Exception) { onFailure(e) }
        }
    }
}