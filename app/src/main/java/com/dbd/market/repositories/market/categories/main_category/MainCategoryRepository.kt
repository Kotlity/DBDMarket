package com.dbd.market.repositories.market.categories.main_category

import com.google.firebase.firestore.QuerySnapshot

interface MainCategoryRepository {

    fun getSpecialProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit)

    fun getBeneficialProductsFromFirebaseFirestore(onSuccess: (QuerySnapshot) -> Unit, onFailure: (Exception) -> Unit)
}