package com.dbd.market.repositories.market.categories.torso

import com.google.firebase.firestore.QuerySnapshot

interface TorsoCategoryRepository {

    suspend fun getTorsoProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)
}