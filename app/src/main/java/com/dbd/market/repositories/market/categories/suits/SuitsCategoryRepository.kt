package com.dbd.market.repositories.market.categories.suits

import com.google.firebase.firestore.QuerySnapshot

interface SuitsCategoryRepository {

    suspend fun getSuitsCategoryFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)
}