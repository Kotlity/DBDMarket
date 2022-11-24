package com.dbd.market.repositories.market.categories.legs

import com.google.firebase.firestore.QuerySnapshot

interface LegsCategoryRepository {

    suspend fun getLegsProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)
}