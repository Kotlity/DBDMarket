package com.dbd.market.repositories.market.categories.main_category

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION_PATH
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD_VALUE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class MainCategoryRepositoryImplementation @Inject constructor(private val firebaseFirestore: FirebaseFirestore): MainCategoryRepository {

    override fun getSpecialProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION_PATH)
            .whereEqualTo(FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD_VALUE)
            .get().addOnSuccessListener { querySnapshot ->
                onSuccess(querySnapshot)
            }
            .addOnFailureListener { fetchingFromFirebaseFirestoreException ->
                onFailure(fetchingFromFirebaseFirestoreException)
            }
    }
}