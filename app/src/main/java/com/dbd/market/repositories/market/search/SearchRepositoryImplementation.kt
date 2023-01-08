package com.dbd.market.repositories.market.search

import com.dbd.market.data.Product
import com.dbd.market.di.qualifiers.ProductsCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_NAME_FIELD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class SearchRepositoryImplementation @Inject constructor(@ProductsCollectionReference private val productsCollectionReference: CollectionReference): SearchRepository {

    override fun searchProducts(searchQuery: String, onSuccess: (List<Product>) -> Unit, onFailure: (String) -> Unit) {
        productsCollectionReference.orderBy(FIREBASE_FIRESTORE_PRODUCTS_NAME_FIELD)
            .startAt(searchQuery)
            .endAt(searchQuery + "\uf8ff")
            .get()
            .addOnSuccessListener { searchProductsQuerySnapshot ->
                val listOfSearchedProducts = searchProductsQuerySnapshot.toObjects<Product>()
                onSuccess(listOfSearchedProducts)
            }
            .addOnFailureListener { searchProductsError -> onFailure(searchProductsError.message.toString()) }
    }

}