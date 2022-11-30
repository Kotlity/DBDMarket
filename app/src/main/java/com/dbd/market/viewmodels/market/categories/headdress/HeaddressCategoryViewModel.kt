package com.dbd.market.viewmodels.market.categories.headdress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.PagingInfo
import com.dbd.market.data.Product
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

    private val _headdressProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val headdressProfitableProducts = _headdressProfitableProducts.asStateFlow()

    private val _headdressOtherProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val headdressOtherProducts = _headdressOtherProducts.asStateFlow()

    private val headdressOtherPagingInfo = MutableStateFlow(PagingInfo())

    init {
        getHeaddressProfitableProducts()
        getHeaddressOtherProducts()
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

    fun getHeaddressOtherProducts() {
        if (!headdressOtherPagingInfo.value.isEndOfPaging) {
            viewModelScope.launch(Dispatchers.IO) {
                headdressCategoryRepository.getHeaddressOtherProductsFromFirebaseFirestore(onSuccess = { headdressOtherQuerySnapshot ->
                    val convertHeaddressOtherQuerySnapshotToHeaddressProductObject = headdressOtherQuerySnapshot.toObjects<Product>()
                    _headdressOtherProducts.emit(Resource.Success(convertHeaddressOtherQuerySnapshotToHeaddressProductObject))
                    headdressOtherPagingInfo.value.isEndOfPaging = convertHeaddressOtherQuerySnapshotToHeaddressProductObject == headdressOtherPagingInfo.value.oldProducts
                    headdressOtherPagingInfo.value.oldProducts = convertHeaddressOtherQuerySnapshotToHeaddressProductObject
                    headdressOtherPagingInfo.value.pageNumber++
                },
                onFailure = { headdressOtherException ->
                    _headdressOtherProducts.emit(Resource.Error(headdressOtherException.message.toString()))
                },
                pageNumber = headdressOtherPagingInfo.value.pageNumber)
            }
        }
    }
}