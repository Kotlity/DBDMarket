package com.dbd.market.viewmodels.market.categories.torso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.torso.TorsoCategoryRepository
import com.dbd.market.utils.Resource
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TorsoCategoryViewModel @Inject constructor(private val torsoCategoryRepository: TorsoCategoryRepository): ViewModel() {

    private var _torsoProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val torsoProfitableProducts = _torsoProfitableProducts.asStateFlow()

    init {
        getTorsoProfitableProducts()
    }

    private fun getTorsoProfitableProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            torsoCategoryRepository.getTorsoProfitableProductsFromFirebaseFirestore(onSuccess = { torsoProfitableQuerySnapshot ->
                val convertTorsoProfitableQuerySnapshotToTorsoProductObject = torsoProfitableQuerySnapshot.toObjects<Product>()
                _torsoProfitableProducts.emit(Resource.Success(convertTorsoProfitableQuerySnapshotToTorsoProductObject))
            },
            onFailure = { torsoProfitableException ->
                _torsoProfitableProducts.emit(Resource.Error(torsoProfitableException.message.toString()))
            })
        }
    }
}