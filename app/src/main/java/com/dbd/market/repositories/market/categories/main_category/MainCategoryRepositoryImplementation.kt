package com.dbd.market.repositories.market.categories.main_category

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_BENEFICIAL_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_INTERESTING_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD_VALUE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class MainCategoryRepositoryImplementation @Inject constructor(private val firebaseFirestore: FirebaseFirestore): MainCategoryRepository {

    override fun getSpecialProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
            .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD_VALUE)
            .get().addOnSuccessListener { specialProductsQuerySnapshot ->
                onSuccess(specialProductsQuerySnapshot)
            }
            .addOnFailureListener { fetchingSpecialProductsException ->
                onFailure(fetchingSpecialProductsException)
            }
    }

    override fun getBeneficialProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit, pageNumber: Long) {
        firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
            .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_BENEFICIAL_PRODUCTS_CATEGORY_FIELD_VALUE)
            .limit(pageNumber * 4)
            .get().addOnSuccessListener { beneficialProductsQuerySnapshot ->
                onSuccess(beneficialProductsQuerySnapshot)
            }
            .addOnFailureListener { fetchingBeneficialProductsException ->
                onFailure(fetchingBeneficialProductsException)
            }
    }

    override fun getInterestingProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit, pageNumber: Long) {
        firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION)
            .whereEqualTo(FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD, FIREBASE_FIRESTORE_INTERESTING_PRODUCTS_CATEGORY_FIELD_VALUE)
            .limit(pageNumber * 4)
            .get().addOnSuccessListener { interestingProductsQuerySnapshot ->
                onSuccess(interestingProductsQuerySnapshot)
            }
            .addOnFailureListener { fetchingInterestingProductsException ->
                onFailure(fetchingInterestingProductsException)
            }
    }
}