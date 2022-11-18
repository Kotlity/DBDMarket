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

    private var _suitsOtherProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val suitsOtherProducts = _suitsOtherProducts.asStateFlow()

    init {
        getSuitsDiscountProducts()
        getSuitsOtherProducts()
    }

    private fun getSuitsDiscountProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            suitsCategoryRepository.getSuitsProfitableCategoryFromFirebaseFirestore(onSuccess = { suitsProfitableQuerySnapshot ->
                val convertSuitsProfitableQuerySnapshotToSuitsProductObject = suitsProfitableQuerySnapshot.toObjects<Product>()
                _suitsProfitableProducts.emit(Resource.Success(convertSuitsProfitableQuerySnapshotToSuitsProductObject))
            },
            onFailure = { exception ->
                _suitsProfitableProducts.emit(Resource.Error(exception.message.toString()))
            })
        }
    }

    private fun getSuitsOtherProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            suitsCategoryRepository.getSuitsOtherCategoryFromFirebaseFirestore(onSuccess = { suitsOtherQuerySnapshot ->
                val convertSuitsOtherSnapshotToSuitsProductObject = suitsOtherQuerySnapshot.toObjects<Product>()
                _suitsOtherProducts.emit(Resource.Success(convertSuitsOtherSnapshotToSuitsProductObject))
            },
            onFailure = { exception ->
                _suitsOtherProducts.emit(Resource.Error(exception.message.toString()))
            })
        }
    }
}