package com.dbd.market.repositories.market.categories.weapons

import com.google.firebase.firestore.QuerySnapshot

interface WeaponsCategoryRepository {

    suspend fun getWeaponsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)

    suspend fun getWeaponsOtherCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long)
}