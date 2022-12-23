package com.dbd.market.repositories.market.orders

import com.dbd.market.data.Order
import com.dbd.market.di.qualifiers.UserOrderCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_ORDERS_DATE_FIELD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class OrdersRepositoryImplementation @Inject constructor(@UserOrderCollectionReference private val userOrderCollectionReference: CollectionReference?): OrdersRepository {

    override fun getAllOrders(onSuccess: (List<Order>) -> Unit, onFailure: (String) -> Unit) {
        userOrderCollectionReference?.orderBy(FIREBASE_FIRESTORE_ORDERS_DATE_FIELD, Query.Direction.DESCENDING)
            ?.get()
            ?.addOnSuccessListener { sortedOrdersQuerySnapshot ->
                val sortedListOfOrders = sortedOrdersQuerySnapshot.toObjects<Order>()
                onSuccess(sortedListOfOrders)
            }
            ?.addOnFailureListener { gettingSortedOrdersError -> onFailure(gettingSortedOrdersError.message.toString()) }
    }
}