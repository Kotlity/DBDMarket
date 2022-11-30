package com.dbd.market.viewmodels.market.categories.legs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.PagingInfo
import com.dbd.market.data.Product
import com.dbd.market.repositories.market.categories.legs.LegsCategoryRepository
import com.dbd.market.utils.Resource
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LegsCategoryViewModel @Inject constructor(private val legsCategoryRepository: LegsCategoryRepository): ViewModel() {

    private val _legsProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val legsProfitableProducts = _legsProfitableProducts.asStateFlow()

    private val _legsOtherProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val legsOtherProducts = _legsOtherProducts.asStateFlow()

    private val legsOtherProductsPagingInfoState = MutableStateFlow(PagingInfo())

    init {
        getLegsProfitableProducts()
        getLegsOtherProducts()
    }

    private fun getLegsProfitableProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            legsCategoryRepository.getLegsProfitableProductsFromFirebaseFirestore(onSuccess = { legsProfitableQuerySnapshot ->
                val convertLegsProfitableQuerySnapshotToLegsProductObject = legsProfitableQuerySnapshot.toObjects<Product>()
                _legsProfitableProducts.emit(Resource.Success(convertLegsProfitableQuerySnapshotToLegsProductObject))
            },
            onFailure = { legsProfitableException ->
                _legsProfitableProducts.value = Resource.Error(legsProfitableException.message.toString())
            })
        }
    }

    fun getLegsOtherProducts() {
        if (!legsOtherProductsPagingInfoState.value.isEndOfPaging) {
            viewModelScope.launch(Dispatchers.IO) {
                legsCategoryRepository.getLegsOtherProductsFromFirebaseFirestore(onSuccess = { legsOtherQuerySnapshot ->
                    val convertLegsOtherQuerySnapshotToLegsProductObject = legsOtherQuerySnapshot.toObjects<Product>()
                    _legsOtherProducts.emit(Resource.Success(convertLegsOtherQuerySnapshotToLegsProductObject))
                    legsOtherProductsPagingInfoState.value.isEndOfPaging = convertLegsOtherQuerySnapshotToLegsProductObject == legsOtherProductsPagingInfoState.value.oldProducts
                    legsOtherProductsPagingInfoState.value.oldProducts = convertLegsOtherQuerySnapshotToLegsProductObject
                    legsOtherProductsPagingInfoState.value.pageNumber++
                },
                onFailure = { legsOtherException ->
                    _legsOtherProducts.emit(Resource.Error(legsOtherException.message.toString()))
                }, pageNumber = legsOtherProductsPagingInfoState.value.pageNumber)
            }
        }
    }
}