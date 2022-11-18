package com.dbd.market.viewmodels.market.categories.suits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.suits.SuitsCategoryRepository
import com.dbd.market.utils.Resource
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuitsCategoryViewModel @Inject constructor(private val suitsCategoryRepository: SuitsCategoryRepository): ViewModel() {

    private var _suitsProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val suitsProfitableProducts = _suitsProfitableProducts.asStateFlow()

    init {
        getSuitsDiscountProducts()
    }

    private fun getSuitsDiscountProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            suitsCategoryRepository.getSuitsCategoryFromFirebaseFirestore(onSuccess = { querySnapshot ->
                val convertSuitsQuerySnapshotToSuitsProductObject = querySnapshot.toObjects<Product>()
                _suitsProfitableProducts.emit(Resource.Success(convertSuitsQuerySnapshotToSuitsProductObject))
            },
            onFailure = { exception ->
                _suitsProfitableProducts.emit(Resource.Error(exception.message.toString()))
            })
        }
    }
}