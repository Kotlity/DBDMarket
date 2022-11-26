package com.dbd.market.repositories.market.categories.weapons

import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE
import com.dbd.market.utils.getSomeOtherProductsFromFirebaseFirestore
import com.dbd.market.utils.getSomeProfitableProductsFromFirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class WeaponsCategoryRepositoryImplementation @Inject constructor(private val productsCollectionReference: CollectionReference): WeaponsCategoryRepository {

    override suspend fun getWeaponsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit) {
        getSomeProfitableProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE)
    }

    override suspend fun getWeaponsOtherCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long) {
        getSomeOtherProductsFromFirebaseFirestore(productsCollectionReference, onSuccess, onFailure, FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE, pageNumber)
    }
}