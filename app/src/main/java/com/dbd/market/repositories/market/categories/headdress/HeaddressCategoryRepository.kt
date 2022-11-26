package com.dbd.market.repositories.market.categories.headdress

import com.google.firebase.firestore.QuerySnapshot

interface HeaddressCategoryRepository {

    suspend fun getHeaddressProfitableProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit)

    suspend fun getHeaddressOtherProductsFromFirebaseFirestore(onSuccess: suspend (QuerySnapshot) -> Unit, onFailure: suspend (Exception) -> Unit, pageNumber: Long)
}