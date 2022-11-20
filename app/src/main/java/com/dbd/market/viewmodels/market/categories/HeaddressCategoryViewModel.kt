package com.dbd.market.viewmodels.market.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.headdress.HeaddressCategoryRepository
import com.dbd.market.utils.Resource
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeaddressCategoryViewModel @Inject constructor(private val headdressCategoryRepository: HeaddressCategoryRepository): ViewModel() {

    private var _headdressProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val headdressProfitableProducts = _headdressProfitableProducts.asStateFlow()

    init {
        getHeaddressProfitableProducts()
    }

    private fun getHeaddressProfitableProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            headdressCategoryRepository.getHeaddressProfitableProductsFromFirebaseFirestore(onSuccess = { headdressProfitableQuerySnapshot ->
                val convertHeaddressProfitableQuerySnapshotToHeaddressProductObject = headdressProfitableQuerySnapshot.toObjects<Product>()
                _headdressProfitableProducts.emit(Resource.Success(convertHeaddressProfitableQuerySnapshotToHeaddressProductObject))
            },
            onFailure = { headdressProfitableException ->
                _headdressProfitableProducts.emit(Resource.Error(headdressProfitableException.message.toString()))
            })
        }
    }
}