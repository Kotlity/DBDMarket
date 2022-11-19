package com.dbd.market.repositories.market.categories.suits

import com.google.firebase.firestore.QuerySnapshot

interface SuitsCategoryRepository {

    suspend fun getSuitsProfitableCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)

    suspend fun getSuitsOtherCategoryFromFirebaseFirestore(onSuccess: suspend(QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long)
}