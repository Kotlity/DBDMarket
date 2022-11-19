package com.dbd.market.repositories.market.categories.weapons

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WeaponsCategoryRepositoryImplementation @Inject constructor(private val firebaseFirestore: FirebaseFirestore): WeaponsCategoryRepository {

    override suspend fun getWeaponsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val weaponsProfitableQuerySnapshot = firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
                    .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE)
                    .whereNotEqualTo(FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD, null)
                    .get().await()
                if (!weaponsProfitableQuerySnapshot.isEmpty) onSuccess(weaponsProfitableQuerySnapshot)
            } catch (e: Exception) { onFailure(e) }
        }
    }
}